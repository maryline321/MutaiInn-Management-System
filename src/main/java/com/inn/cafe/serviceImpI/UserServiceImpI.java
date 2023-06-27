package com.inn.cafe.serviceImpI;

import com.inn.cafe.POJO.User;
import com.inn.cafe.consents.CafeConsents;
import com.inn.cafe.dao.UserDao;
import com.inn.cafe.service.UserService;
import com.inn.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpI implements UserService {
    @Autowired
    UserDao userDao;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {

        log.info("Inside Signup {}",requestMap);
        try {


        if(validateSignUpMap(requestMap)){
            User user = userDao.findByEmailId(requestMap.get("email"));

            if(Objects.isNull(user)){
                userDao.save(getuserFromMap(requestMap));
                return CafeUtils.getResponseEntity("Successfully Registered.",HttpStatus.OK);

            }else {
                return CafeUtils.getResponseEntity("Email already exists.",HttpStatus.BAD_REQUEST);
            }

        }else {
            return CafeUtils.getResponseEntity(CafeConsents.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConsents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
       if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")&&
                requestMap.containsKey("email") && requestMap.containsKey("password"))
       {
           return true;
       }
       return false;

    }

    private User getuserFromMap(Map<String, String> requestMap){
        User user= new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus(requestMap.get("status"));
        return user;

    }
}
