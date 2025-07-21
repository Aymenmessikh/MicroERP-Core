package com.example.adminservice.Controller;

import com.example.adminservice.Config.filter.clause.Clause;
import com.example.adminservice.Config.filter.clause.ClauseOneArg;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.Critiria;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.SearchValue;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.SortParam;
import com.example.adminservice.Dto.Profile.ProfileResponse;
import com.example.adminservice.Dto.User.UserProfileInfoDto;
import com.example.adminservice.Dto.User.UserRequest;
import com.example.adminservice.Dto.User.UserResponse;
import com.example.adminservice.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/user")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping("{id}")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest, @PathVariable Long id){
        UserResponse userResponse = userService.createUser(userRequest, id);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('READ_ALL_USERS')")
    @GetMapping
    public ResponseEntity<PageImpl<UserResponse>> getAllUsers(@Critiria List<Clause> filter,
                                                              @SearchValue ClauseOneArg searchValue,
                                                              @SortParam PageRequest pageRequest) {
        filter.add(searchValue);
        PageImpl<UserResponse> userResponses = userService.getAllUsers(filter,pageRequest);
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }
    @GetMapping("get")
    public ResponseEntity<List<UserResponse>> getUsers(){
        List<UserResponse> userResponses=userService.getUsers();
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_USER')")
    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        UserResponse userResponse = userService.getUserById(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_USER')")
    @GetMapping("byEmail/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email){
        UserResponse userResponse = userService.getUserByEmail(email);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @PutMapping("{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest, @PathVariable Long id){
        UserResponse userResponse = userService.updateUser(userRequest, id);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('DELETE_USER')")
    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @PreAuthorize("hasAuthority('MANAGE_USER_STATUS')")
    @PutMapping("enableDisable/{id}/{enable}")
    public ResponseEntity<UserResponse> enableDisableUser(@PathVariable Long id, @PathVariable Boolean enable) {
        System.out.println("enableDisableUser called with id: " + id + " and enable: " + enable);
       UserResponse userResponse= userService.enableDisabeleUser(id, enable);
       return new ResponseEntity<>(userResponse,HttpStatus.CREATED);
    }
    @GetMapping("count")
    public ResponseEntity<Long> count() {
        Long count= userService.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    @GetMapping("countAcifUser")
    public ResponseEntity<Long> countActifUser() {
        Long count= userService.countActifUser();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    @GetMapping("/by-module/{moduleId}")
    public ResponseEntity<List<UserProfileInfoDto>> getUsersByModule(@PathVariable Long moduleId) {
        List<UserProfileInfoDto> result = userService.getUsersByModuleId(moduleId);
        return ResponseEntity.ok(result);
    }
    @GetMapping("getActifProfil/{username}")
    public ResponseEntity<ProfileResponse> changeActifProfile(@PathVariable String username) {
        ProfileResponse profileResponse=userService.getActifeProfile(username);
        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }
    @GetMapping("getInActifProfil/{username}")
    public ResponseEntity<List<ProfileResponse>> changeInActifProfile(@PathVariable String username) {
        List<ProfileResponse> profileResponse=userService.getInActiveProfile(username);
        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }
    @PutMapping("changeActiveProfile/{id}/{username}")
    public ResponseEntity<UserResponse> changeActiveProfile(@PathVariable Long id, @PathVariable String username){
        UserResponse userResponse=userService.changeActiveProfile(id,username);
        return new  ResponseEntity<>(userResponse,HttpStatus.CREATED);
    }
}
