package com.aneebo.rotg.utils;

import com.badlogic.gdx.utils.Array;

public class Cities {
	private Array<City> Cities; 
	
	public Cities() {
		
	}
	
	
	
	public Array<City> getCities() {
		return Cities;
	}



	public void setCities(Array<City> cities) {
		this.Cities = cities;
	}



	public static class City {
		private String cityName;
		private int XpThreshold;
		private Array<String> arena;
		
		public City() {
			
		}

		public String getCityName() {
			return cityName;
		}

		public void setCityName(String cityName) {
			this.cityName = cityName;
		}

		public Array<String> getArena() {
			return arena;
		}

		public void setArena(Array<String> arena) {
			this.arena = arena;
		}
		
		
	}
}
