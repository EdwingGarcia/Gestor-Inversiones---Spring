package com.edwinggarcia.Inversiones.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.*;
@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "apellido")
	private String apellido;

	private String email;
	private String password;
	private String rol;

	@ElementCollection
	@CollectionTable(name = "usuario_emails", joinColumns = @JoinColumn(name = "usuario_id"))
	@Column(name = "email")
	private List<String> emailsAsociados = new ArrayList<>();

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public List<String> getEmailsAsociados() {
		return emailsAsociados;
	}

	public void setEmailsAsociados(List<String> emailsAsociados) {
		this.emailsAsociados = emailsAsociados;
	}

	public Usuario(String apellido, String email, List<String> emailsAsociados, Long id, String nombre, String password, String rol) {
		this.apellido = apellido;
		this.email = email;
		this.emailsAsociados = emailsAsociados;
		this.id = id;
		this.nombre = nombre;
		this.password = password;
		this.rol = rol;
	}

	public Usuario(String nombre, String apellido, String email, String password, String rol) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.password = password;
		this.rol = rol;
	}

	public Usuario() {}
}
