package com.zeedoo.mars.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.zeedoo.commons.domain.FileTransferTask;
import com.zeedoo.commons.domain.FileType;
import com.zeedoo.commons.domain.payload.SensorFileInfo;
import com.zeedoo.commons.domain.payload.SensorFilePacket;
import com.zeedoo.mars.dao.FileTransferTaskDao;
import com.zeedoo.mars.file.transfer.CompleteFile;

@ManagedResource()
@Component
public class FileTransferServiceBean {
	
	private static final String FILE_TRANSFER_LOGGER_NAME = "FileTransferLogger";
	private static final Logger FILE_TRANSFER_LOGGER = LoggerFactory.getLogger(FILE_TRANSFER_LOGGER_NAME);
	
	private static final String TEMP_FILE_DIRECTORY = "tempFiles";
	private static final String COMPLETED_FILES_DIRECTORY = "completedFiles";
	
	// Threshold to determine we need to request Sun to send sensor data over
	// Default to 2048 KB
	private static AtomicInteger SENSOR_DATA_SIZE_THRESHOLD = new AtomicInteger(2048);
	
	// Interval between each message to get sensor data info from Sun (seconds)
	// Default to 30 seconds
	private static AtomicInteger SENSOR_DATA_SYNC_INFO_INTERVAL = new AtomicInteger(30);
	
	// Max interval between two sensor data sync requests
	// If the elapsed time from previous successful data sync is greater than this value, we will request a sensor data tranfser
    // Right now set to 12 hours - 12 hours = 12x60x60 = 43200 seconds
	private static AtomicInteger MAX_SENSOR_DATA_SYNC_WAIT_TIME = new AtomicInteger(43200);
	
	private static AtomicInteger MAX_FILE_PACKET_ERROR_RETRIES = new AtomicInteger(3);
	
	private static Boolean BYPASS_CRC32_CHECK = Boolean.FALSE;	
	private static Boolean BYPASS_MD5_CHECK = Boolean.FALSE;
	
	@Autowired
	private FileTransferTaskDao fileTransferTaskDao;
		
	@Autowired
	private CacheServiceBean cacheService;
	
	@ManagedAttribute(description = "Threshold to determine if we need to request Sun to send sensor data over")
	public int getSensorDataSizeThreshold() {
		return SENSOR_DATA_SIZE_THRESHOLD.get();
	}

	@ManagedAttribute(description = "Set threshold that determines if we need to request Sun to send sensor data over")
	public void setSensorDataSizeThreshold(int threshold) {
		SENSOR_DATA_SIZE_THRESHOLD.set(threshold);
	}

	@ManagedAttribute(description = "Inteval between each message to request sensor data sync info from Sun")
	public int getSensorDataSyncInfoInterval() {
		return SENSOR_DATA_SYNC_INFO_INTERVAL.get();
	}

	@ManagedAttribute(description = "Get inteval between each message to request sensor data sync info from Sun")
	public void setSensorDataSyncInfoInterval(int interval) {
		SENSOR_DATA_SYNC_INFO_INTERVAL.set(interval);
	}
	
	@ManagedAttribute(description = "Max time allowed between a successful data sync and next sensor data sync request")
	public int getMaxSensorDataSyncWaitTime() {
		return MAX_SENSOR_DATA_SYNC_WAIT_TIME.get();
	}
	
	@ManagedAttribute(description = "Sets max time allowed between a successful data sync and next sensor data sync request")
	public void setMaxSensorDataSyncWaitTime(int value) {
		MAX_SENSOR_DATA_SYNC_WAIT_TIME.set(value);
	}
	
	@ManagedAttribute(description = "Maximum retries for file packet errors")
	public int getMaxFilePacketErrorRetries() {
		return MAX_FILE_PACKET_ERROR_RETRIES.get();
	}

	@ManagedAttribute(description = "Sets maximum retry number for file packet errors")
	public void setMaxFilePacketErrorRetries(int value) {
		MAX_FILE_PACKET_ERROR_RETRIES.set(value);
	}
	
	@ManagedAttribute(description = "Whether we bypass CRC32 checks")
	public Boolean getBypassCRC32Check() {
		return BYPASS_CRC32_CHECK;
	}

	@ManagedAttribute(description = "Sets whether we bypass CRC32 checks")
	public void setBypassCRC32Check(Boolean bYPASS_CRC32_CHECK) {
		BYPASS_CRC32_CHECK = bYPASS_CRC32_CHECK;
	}

	@ManagedAttribute(description = "Whether we bypass MD5 checks")
	public Boolean getBypassMD5Check() {
		return BYPASS_MD5_CHECK;
	}

	@ManagedAttribute(description = "Sets hether we bypass MD5 checks")
	public static void setBypassMD5Check(Boolean bYPASS_MD5_CHECK) {
		BYPASS_MD5_CHECK = bYPASS_MD5_CHECK;
	}

	/**
	 * Returns the FileTransfer info object by Sun MAC address
	 * @param sunMacAddress
	 * @return
	 */
	public FileTransferTask getFileTransferTask(String sunMacAddress, FileType fileType, String fileId) {
		return fileTransferTaskDao.get(sunMacAddress, fileId, fileType);
	}
	
	/**
	 * Creates a new file transfer info object with the given SensorFileInfo and put into the cache
	 * @param sunMacAddress
	 * @param sfi
	 * @throws IOException 
	 */
	public void newFileTransferTask(String sunMacAddress, SensorFileInfo sfi) throws IOException {
		Preconditions.checkArgument(sunMacAddress != null, "Sun MAC address should not be null");
		Joiner joiner = Joiner.on("-").skipNulls();
		String filename = joiner.join(sunMacAddress, FileType.SENSOR_DATA_READINGS.getValue(), sfi.getSensorId());
		File file = new File(TEMP_FILE_DIRECTORY + "/" + filename);
		File parent = file.getAbsoluteFile().getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}
		FILE_TRANSFER_LOGGER.debug("Successfully created file on disk, filename={}", filename);
		FileTransferTask task = new FileTransferTask(sunMacAddress, sfi.getSensorId(), file.getAbsolutePath(), sfi.getNumberOfPackets(), sfi.getPacketStartNumber(), sfi.getMd5(), FileType.SENSOR_DATA_READINGS);
		fileTransferTaskDao.insertOrReplace(task);
		FILE_TRANSFER_LOGGER.info("File transfer task created={}", task);
	}
	
	/**
	 * Update existing fileTransfer info object
	 * @param sunMacAddress
	 * @param fib
	 */
	public void updateFileTransferTask(String sunMacAddress, FileTransferTask updatedFileTransferTask) {
		fileTransferTaskDao.update(updatedFileTransferTask);
		FILE_TRANSFER_LOGGER.info("Updated fileTransferTask, new state={}", updatedFileTransferTask);
	}
	
	/**
	 * Remove an entry
	 * @param sunMacAddress
	 */
	public void removeFileTransferTask(String sunMacAddress, FileType fileType, String fileId) {
		Preconditions.checkArgument(sunMacAddress != null, "Sun MAC address should not be null");
		String fields = generateUniqueKey(sunMacAddress, fileType, fileId);
		if (fileTransferTaskDao.delete(sunMacAddress, fileId, fileType)) {
			FILE_TRANSFER_LOGGER.info("Removed fileTransferTask for fields={}", fields);
		} else {
			FILE_TRANSFER_LOGGER.warn("Unable to find and remove FileTransferTask with fields={}", fields);
		}
	}
	
	/**
	 * 
	 * @param task
	 * @param packet
	 * @return DecodedFile if the packet is the last packet
	 * @throws NoSuchAlgorithmException 
	 */
	public Optional<CompleteFile> processFilePacket(FileTransferTask task, SensorFilePacket packet) throws IOException, NoSuchAlgorithmException {
		final int sensorFilePacketNumber = packet.getPacketNumber();
		if (isCurrentPacketLastPacket(sensorFilePacketNumber, task.getNumberOfPackets())) {
			// append data into file
		    appendDataToFile(task, packet);
		    // compute the MD5 hash from the complete file
			String base64String = FileUtils.readFileToString(new File(task.getFilePath()), StandardCharsets.UTF_8);
		    String md5 = computeMD5Hash(base64String);
		    return Optional.fromNullable(new CompleteFile(md5, base64String));
		} else {
			appendDataToFile(task, packet);
			return Optional.absent();
		}		
	}
	
    /**
     * Decodes the base 64 encoded and write it to a new file
     * @param base64 encoded string
     * @param fileTransferTask
     * @return path to the file
     * @throws IOException
     */
	public String decodeFile(String base64String, FileTransferTask fileTransferTask) throws IOException {
		final byte[] fileBytes = Base64.decodeBase64(base64String);
		Joiner joiner = Joiner.on("-").skipNulls();
		String filename = joiner.join(fileTransferTask.getSunMacAddress(), FileType.SENSOR_DATA_READINGS.name(), fileTransferTask.getFileId());
		try {
			File f = new File(COMPLETED_FILES_DIRECTORY, filename);
			FileUtils.writeByteArrayToFile(f, fileBytes);
			FILE_TRANSFER_LOGGER.info("Successfully wrote complete file to disk, filename={}", filename);
            return f.getAbsolutePath();
		} catch (IOException e) {
			FILE_TRANSFER_LOGGER.error("Error writing file to disk, attempted filename={}", filename);
			throw e;
		}
		
	}

	/**
	 * Generates a unique key for given object type/ids
	 * @param sunMacAddress
	 * @param fileType
	 * @param fileId
	 * @return
	 */
	private String generateUniqueKey(String sunMacAddress, FileType fileType, String fileId) {
		Joiner joiner = Joiner.on(":").skipNulls();
		return joiner.join(sunMacAddress, fileType.name(), fileId);
	}
	
	/**
	 * Check if the current packet is the last packet in the file
	 * @param sensorFilePacketNumber
	 * @param numberOfPackets
	 * @return
	 */
	private boolean isCurrentPacketLastPacket(int sensorFilePacketNumber, int numberOfPackets) {
		return sensorFilePacketNumber == (numberOfPackets - 1);
	}
	
	/**
	 * Append a file packet to the temporary file
	 * @param task
	 * @param packet
	 * @throws IOException
	 */
	private void appendDataToFile(FileTransferTask task, SensorFilePacket packet)
			throws IOException {
		FileWriter fw = new FileWriter(task.getFilePath(), true);
		fw.write(packet.getData());
		fw.close();
	}

	/**
	 * Computes the MD5 hash given a base64 encoded file data
	 * @param file
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String computeMD5Hash(String file) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(file.getBytes());
		byte byteData[] = md.digest();
		StringBuffer sb = new StringBuffer();
		// convert to hex
        for (int i = 0; i < byteData.length; i++) {
        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
}
