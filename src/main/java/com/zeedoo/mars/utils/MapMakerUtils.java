package com.zeedoo.mars.utils;

import com.google.common.collect.MapMaker;

public class MapMakerUtils {
	
	private MapMakerUtils() {
		//Hidden on purpose
	}
	
	private static final MapMaker MAP_MAKER = new MapMaker();
	
    public MapMaker getMapMaker() {
    	return MAP_MAKER;
    }
}
