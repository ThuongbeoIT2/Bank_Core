package com.example.secumix.security.notify;

import com.example.secumix.security.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.Date;
@Data
@Entity(name = "notify")
@Table(name = "notify")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notiId;
    @Column(nullable = false,name = "description")
    @NotBlank
    private String description;
    @Column(name = "notiStatus")
    private boolean notiStatus;
    @Column(name = "deletedNoti")
    private boolean deletedNoti;
    @Column(name = "createdAt")
    private Date createdAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userID",foreignKey = @ForeignKey(name = "fk_user_notify"))
    private User user;
}
