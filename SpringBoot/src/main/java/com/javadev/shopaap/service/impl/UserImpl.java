package com.javadev.shopaap.service.impl;

import com.javadev.shopaap.components.JwtTokenUtil;
import com.javadev.shopaap.dto.UserDTO;
import com.javadev.shopaap.entity.RoleEntity;
import com.javadev.shopaap.entity.UserEntity;
import com.javadev.shopaap.exception.DataNotFoundException;
import com.javadev.shopaap.repositories.RoleRepository;
import com.javadev.shopaap.repositories.UserRepository;
import com.javadev.shopaap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public UserEntity createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNum = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNum)) {
            throw new DataIntegrityViolationException("Phone already exits");
        }
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        RoleEntity role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not Found"));
        userEntity.setRole(role);
        if (userDTO.getFacebookAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            userEntity.setPassword(encodedPassword);
        }

        return userRepository.save(userEntity);
    }

    @Override
    public String login(String phoneNumber, String passWord) throws Exception {
        Optional<UserEntity> optionalUserEntity = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUserEntity.isEmpty())
            throw new DataNotFoundException("Invalid phoneNumber/password");

        UserEntity existingUser=optionalUserEntity.get();
        if(existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0){
            if(!passwordEncoder.matches(passWord,existingUser.getPassword())){
                throw new BadCredentialsException("Wrong phoneNumber or PassWord");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
            phoneNumber,passWord,
                existingUser.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }
}
