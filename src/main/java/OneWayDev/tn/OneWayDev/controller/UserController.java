package OneWayDev.tn.OneWayDev.controller;

import OneWayDev.tn.OneWayDev.dto.request.AgencyRequest;
import OneWayDev.tn.OneWayDev.dto.request.AgencyRequestManage;
import OneWayDev.tn.OneWayDev.dto.request.ClientRegisterRequest;
import OneWayDev.tn.OneWayDev.dto.request.CustomErrorResponse;
import OneWayDev.tn.OneWayDev.entity.User;
import OneWayDev.tn.OneWayDev.Repository.UserRepository;
import OneWayDev.tn.OneWayDev.Service.UserService;
import OneWayDev.tn.OneWayDev.exception.EmailExistsExecption;
import OneWayDev.tn.OneWayDev.exception.NotFoundExecption;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("user")
@CrossOrigin("*")
@Validated
public class UserController {

    private UserService userService;
    private UserRepository userRepository;

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable(value = "email") String email){
        return userService.getUserByEmail(email);
    }
    @GetMapping("/findById/{idUser}")
    public User findUserById(@PathVariable(value = "idUser") Long idUser){
        return userService.findUserById(idUser);
    }
    @PostMapping("/register-agency")
    public ResponseEntity<?> registerAgency(@ModelAttribute @Valid AgencyRequest registerRequestDTO){
        try {
            return new ResponseEntity<>(userService.registerAgency(registerRequestDTO), HttpStatus.CREATED);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/manage-agence/{idUser}")
    public ResponseEntity<?> manageAgence(@PathVariable(value = "idUser") Long idUser,@ModelAttribute @Valid AgencyRequestManage registerRequestDTO){
        try {
            return new ResponseEntity<>(userService.manageAgence(idUser,registerRequestDTO), HttpStatus.OK);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/all-agency")
    public List<User> getAllAgency(){
        return userService.findAllAgencies();
    }
    @GetMapping("/all-client")
    public List<User> getAllClient(){
        return userService.findAllClient();
    }
    @DeleteMapping("/delete/{idUser}")
    public ResponseEntity<Map<String, String>> removeCategorie(@PathVariable Long idUser) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.deleteUser(idUser);
            response.put("message", "user dropped successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundExecption e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
    @PutMapping("/block/{id}")
    public User blockUser(@PathVariable(value = "id") Long idUser){
        return userService.blockedUser(idUser, false);
    }


    @PutMapping("/unblock/{id}")
    public User unblockUser(@PathVariable(value = "id") Long idUser){
        return userService.blockedUser(idUser, true);
    }

    @PostMapping("/upload-profile-picture")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(@RequestParam("file") MultipartFile file, @RequestParam("email") String email) {
        String filePath = userService.uploadFile(file);
        User user = userService.getUserByEmail(email);
        user.setPhotoProfile(filePath); // assuming User class has a setPhotoProfile method
        userRepository.save(user); // assuming UserService class has a save method

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Profile picture updated successfully");
        responseBody.put("filePath", filePath);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }


 @GetMapping("/image/{imageName}")
public ResponseEntity<org.springframework.core.io.Resource> getImage(@PathVariable String imageName) {
    Path imagePath = Paths.get("src/main/resources/upload").resolve(imageName);
    try {
        org.springframework.core.io.Resource resource = new UrlResource(imagePath.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok().body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    } catch (Exception e) {
        return ResponseEntity.notFound().build();
    }
}
}
