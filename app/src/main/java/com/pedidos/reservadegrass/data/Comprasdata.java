package com.pedidos.reservadegrass.data;public class Comprasdata {	String nombre;	String marca;	int precio;	public Comprasdata(String nombre, String marca, int precio) {		this.nombre = nombre;		this.marca = marca;		this.precio = precio;	}	public Comprasdata() {	}	public String getNombre() {		return nombre;	}	public void setNombre(String nombre) {		this.nombre = nombre;	}	public String getMarca() {		return marca;	}	public void setMarca(String marca) {		this.marca = marca;	}	public int getPrecio() {		return precio;	}	public void setPrecio(int precio) {		this.precio = precio;	}}