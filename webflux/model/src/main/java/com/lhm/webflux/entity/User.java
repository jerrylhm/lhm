package com.lhm.webflux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
        @Id
        private String id;
        @Indexed(unique = true)
        private String username;
        private String phone;
        private String email;
        private String name;
        private Date birthday;
    }