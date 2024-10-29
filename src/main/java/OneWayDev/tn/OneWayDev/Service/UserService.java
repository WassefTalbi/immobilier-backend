package OneWayDev.tn.OneWayDev.Service;

import OneWayDev.tn.OneWayDev.Repository.RoleRepository;
import OneWayDev.tn.OneWayDev.dto.request.AgencyRequest;
import OneWayDev.tn.OneWayDev.dto.request.AgencyRequestManage;
import OneWayDev.tn.OneWayDev.dto.request.ClientRegisterRequest;
import OneWayDev.tn.OneWayDev.entity.MailToken;
import OneWayDev.tn.OneWayDev.entity.Role;
import OneWayDev.tn.OneWayDev.entity.User;
import OneWayDev.tn.OneWayDev.entity.RoleType;
import OneWayDev.tn.OneWayDev.Repository.UserRepository;
import OneWayDev.tn.OneWayDev.dto.request.ProfileRequest;
import OneWayDev.tn.OneWayDev.exception.EmailExistsExecption;
import OneWayDev.tn.OneWayDev.exception.NotFoundExecption;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final FileService fileService;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email " + email));
    }
    public User registerAgency(AgencyRequest registerRequestDTO) {
        try{
            if(userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()){
                throw new EmailExistsExecption("Email already exists");
            }
            Role role=roleRepository.findByRoleType(RoleType.AGENCE).get();
            User user=new User();
            user.setName(registerRequestDTO.getName());
            user.setDescription(registerRequestDTO.getDescription());
            user.setPhone(registerRequestDTO.getMobileNumber());
            user.setPassword(bCryptPasswordEncoder.encode(registerRequestDTO.getPassword()));
            user.setSinceYear(registerRequestDTO.getSinceYear());
            user.setEmail(registerRequestDTO.getEmail());
            user.setAddress(registerRequestDTO.getAddress());
            user.setEnabled(false);
            user.setNonLocked(true);
            user.setRoles(List.of(role));
            String photoName=fileService.uploadFile(registerRequestDTO.getLogo());
            user.setPhotoProfile(photoName);
            return userRepository.save(user);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<User>findAllAgencies( ){
        List<User> agencies = userRepository.findByRolesRoleType(RoleType.AGENCE);
        return agencies;
    }
    public List<User>findAllClient( ){
        List<User> clients = userRepository.findByRolesRoleType(RoleType.USER);
        return clients;
    }


    public User findUserById(Long idUser){
       Optional<User>  user= userRepository.findById(idUser);
       if (!user.isPresent()){
           throw new NotFoundExecption("no user found");
       }
       return user.get();
    }

    public User manageAgence(Long idUser, AgencyRequestManage agencyRequest){
        Optional<User>  findUser= userRepository.findById(idUser);
        if (!findUser.isPresent()){
            throw new NotFoundExecption("no agence found");
        }
        User user= findUser.get();
        user.setName(agencyRequest.getName());
        user.setDescription(agencyRequest.getDescription());
        user.setPhone(agencyRequest.getMobileNumber());
        user.setSinceYear(agencyRequest.getSinceYear());
        user.setEmail(agencyRequest.getEmail());
        user.setAddress(agencyRequest.getAddress());
        if (agencyRequest.getLogo() != null && !agencyRequest.getLogo().isEmpty()) {
            String photoName= fileService.uploadFile(agencyRequest.getLogo());
            user.setPhotoProfile(photoName);
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long idUser){
        User user= userRepository.findById(idUser).orElseThrow(()->new NotFoundExecption(" no user found"));
        userRepository.deleteById(idUser);
    }

    public User enabledUser(Long idUser, Boolean enable){
        Optional<User>  findUser= userRepository.findById(idUser);
        if (!findUser.isPresent()){
            throw new NotFoundExecption("no user found");
        }
        User user= findUser.get();
        user.setEnabled(enable);
        return userRepository.save(user);
    }
    public User blockedUser(Long idUser, Boolean blocked){
        Optional<User>  findUser= userRepository.findById(idUser);
        if (!findUser.isPresent()){
            throw new NotFoundExecption("no user found");
        }

        User user= findUser.get();
        user.setNonLocked(blocked);
        return userRepository.save(user);
    }

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }




   public String uploadFile(MultipartFile file) {
    if (file.isEmpty()) {
        throw new IllegalArgumentException("Photo profile is required, should select a photo");
    }
    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    try {
        if (filename.contains("..")) {
            throw new IllegalArgumentException("Cannot upload file with relative path outside current directory");
        }
        Path uploadDir = Paths.get("src/main/resources/upload");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path filePath = uploadDir.resolve(filename);
        if (Files.exists(filePath)) {
            // If file already exists, return the filename to save it in user's profile
            return filename;
        }

        Files.copy(file.getInputStream(), filePath);
        return filename;

    } catch (Exception e) {
        throw new RuntimeException("Failed to store file " + filename, e);
    }
}

}
