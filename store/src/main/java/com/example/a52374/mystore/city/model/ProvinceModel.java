package com.example.a52374.mystore.city.model;

import java.util.List;


//城市3级联动 省
public class ProvinceModel {
	private String name;
	private List<CityModel> cityList;
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, List<CityModel> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
//		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";

		return  name;
	}
	
}
