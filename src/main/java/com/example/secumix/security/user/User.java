package com.example.secumix.security.user;



import com.example.secumix.security.notify.Notify;
import com.example.secumix.security.token.Token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;

import java.util.Collection;
import java.util.List;

@Data
@Builder

@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Integer id;
  private String firstname;
  private String lastname;
  private String email;
  private String password;
  @Enumerated(EnumType.STRING)
  @Column(name = "auth_type")
  private AuthenticationType authType;

  @Enumerated(EnumType.STRING)
  private Role role;
  @Column(name = "enabled")
  private boolean enabled;

  @Column(name = "online_status")
  private boolean onlineStatus=false;

  @Column(name = "last_logout_time")
  private Long lastLogoutTime;


  @OneToMany(mappedBy = "user")
  private List<Notify> notifies;
  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  public User() {

  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
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
    return enabled;
  }

}
