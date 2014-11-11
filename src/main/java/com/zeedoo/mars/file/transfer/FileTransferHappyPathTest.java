package com.zeedoo.mars.file.transfer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeedoo.commons.domain.payload.SensorFileInfo;
import com.zeedoo.commons.domain.payload.SensorFilePacket;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageBuilder;
import com.zeedoo.mars.message.MessageSerializer;
import com.zeedoo.mars.message.MessageType;

public class FileTransferHappyPathTest {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private static String getUuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String hostName = "localhost";
		String sunFakeID = UUID.randomUUID().toString();
		int portNumber = 2112;
		SensorFileInfo info = new SensorFileInfo();
		info.setFileSize(1000);
		info.setMd5("3");
		info.setNumberOfPackets(10);
		info.setPacketStartNumber(0);
		info.setSensorId("123456");
		Message sensorFileInfo = MessageBuilder.buildMessage(MessageType.SENSOR_FILE_INFO);
		sensorFileInfo.setPayload(MAPPER.valueToTree(info));
		sensorFileInfo.setSource("Sun");
		sensorFileInfo.setSourceId(sunFakeID);
		String sensorFileInfoJson = MessageSerializer.toJSON(sensorFileInfo) + '\0';
		List<String> packets = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			Message packetMsg = MessageBuilder.buildMessage(MessageType.SENSOR_FILE_PACKET);
			packetMsg.setSource("Sun");
			packetMsg.setSourceId(sunFakeID);
			SensorFilePacket packet = new SensorFilePacket();
			packet.setCRC32("0");
			packet.setData("MTIzNA==");
			packet.setPacketNumber(i);
			packet.setPacketSize(100);
			packet.setSensorId("123456");
			packetMsg.setPayload(MAPPER.valueToTree(packet));
			packets.add(MessageSerializer.toJSON(packetMsg) + "\0");
		}
		
		try {
			while(true) {
				Socket socket = new Socket(hostName, portNumber);
				DataOutputStream oos = null;
				oos = new DataOutputStream(socket.getOutputStream());
				oos.writeBytes(sensorFileInfoJson + '\0');
				for (int i = 0; i < packets.size(); i++) {
					oos.writeBytes(packets.get(i));
					//oos.writeBytes(packets.get(i));
				}
				Message response = MessageBuilder.buildResponseMessage(MessageType.RESPONSE_SENSOR_FILEDATA_TRANSFER, "2", "");
				response.setSource("Sun");
				response.setSourceId(sunFakeID);
				String responseJson = MessageSerializer.toJSON(response) + '\0';
				oos.writeBytes(responseJson);
				//oos.close();
				Thread.sleep(100000000);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
