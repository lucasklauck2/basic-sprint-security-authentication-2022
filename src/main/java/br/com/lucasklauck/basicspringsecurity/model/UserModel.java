package br.com.lucasklauck.basicspringsecurity.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name ="tb_user")
public class UserModel implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id_user")
	private Long id;
	
	@Column(name = "tx_name", nullable = false)
	private String name;
	
	@Column(name = "tx_email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "tx_password", nullable = false)
	private String password;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_registration")
	private Date dateRegistration;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_update")
	private Date dateUpdate;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		
		return password;
	}

	@Override
	public String getUsername() {
		
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}
}
