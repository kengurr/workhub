package com.workhub.service;

import com.workhub.dto.ChangePasswordRequest;

import java.security.Principal;

public interface UserService {

    void changePassword(ChangePasswordRequest request, Principal connectedUser);
}
