package com.example.adminservice.Controller;

import com.example.adminservice.Services.PermisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin/permission")
@RequiredArgsConstructor
public class PermissionController {
    private final PermisionService permisionService;

    //    @GetMapping
//    public ResponseEntity<Boolean> verefierPermission(@RequestParam String authority,
//                                                      @RequestParam String module,
//                                                      @RequestParam String userName){
//        Boolean permission=permisionService.verfierPermission(authority,module,userName);
//        return new ResponseEntity<>(permission, HttpStatus.OK);
//    }
//    @GetMapping
//    public ResponseEntity<List<Authority>> verefierPermission(@RequestParam String authority,
//                                                                     @RequestParam String module,
//                                                                     @RequestParam String userName){
//        List<Authority> permission=permisionService.verfierPermission(authority,module,userName);
//        return new ResponseEntity<>(permission, HttpStatus.OK);
//    }
    @GetMapping
    public ResponseEntity<Object> getPermissionForUser(@RequestParam String authority,
                                                       @RequestParam String module,
                                                       @RequestParam String username) {
        try {
            return new ResponseEntity<>(permisionService.getPermissionForUser(authority, module,username), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
