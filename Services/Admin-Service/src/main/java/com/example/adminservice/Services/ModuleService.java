package com.example.adminservice.Services;

import com.example.adminservice.Config.Exceptions.MyResourceNotFoundException;
import com.example.adminservice.Config.Exceptions.RessourceAlreadyDisabledException;
import com.example.adminservice.Config.Exceptions.RessourceAlreadyEnabledException;
import com.example.adminservice.Dto.Module.ModuleRequest;
import com.example.adminservice.Dto.Module.ModuleResponse;
import com.example.adminservice.Entity.Module;
import com.example.adminservice.Entity.Profile;
import com.example.adminservice.Entity.User;
import com.example.adminservice.Mapper.Module.ModuleMapper;
import com.example.adminservice.Repository.ModuleRepository;
import com.example.adminservice.Repository.ProfileRepository;
import com.example.adminservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    private String uploadDir="C:\\Users\\USER\\Desktop\\Project\\NFR\\Services\\Admin-Service\\src\\main\\resources\\file";

    public ModuleResponse createModule(ModuleRequest moduleRequest) {
        Module module = moduleRepository.save(moduleMapper.EntityFromDto(moduleRequest));
        return moduleMapper.DtoFromEntity(module);
    }
    public ModuleResponse createModuleWithIcon(ModuleRequest moduleRequest, MultipartFile iconFile) {
        try {
            // Sauvegarder l'image et obtenir le chemin
            String iconPath = saveIconFile(iconFile);

            // Mettre à jour la requête avec le chemin de l'icône
            moduleRequest.setIcon(iconPath);

            // Créer le module
            Module module = moduleMapper.EntityFromDto(moduleRequest);
            module = moduleRepository.save(module);

            return moduleMapper.DtoFromEntity(module);

        } catch (IOException e) {
            throw new RuntimeException("Échec de la sauvegarde de l'icône", e);
        }
    }

    private String saveIconFile(MultipartFile file) throws IOException {
        // Créer le dossier s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un nom de fichier unique
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // Chemin complet du fichier
        Path filePath = uploadPath.resolve(uniqueFilename);

        // Sauvegarder le fichier
        Files.copy(file.getInputStream(), filePath);

        // Retourner le chemin relatif
        return uploadDir + "/" + uniqueFilename;
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex >= 0 ? filename.substring(lastDotIndex) : "";
    }

    // Méthode pour servir les images (optionnelle)
    public byte[] getIconFile(String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename);
        if (!Files.exists(filePath)) {
            throw new MyResourceNotFoundException("Fichier non trouvé: " + filename);
        }
        return Files.readAllBytes(filePath);
    }

    public List<ModuleResponse> getAllModules() {
        return moduleRepository.findAll().stream().map(moduleMapper::DtoFromEntity).collect(Collectors.toList());
    }

    public ModuleResponse getModuleById(Long id) {
        return moduleMapper.DtoFromEntity(moduleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + id)));
    }

    public ModuleResponse getModuleByCode(String code) {
        return moduleMapper.DtoFromEntity(moduleRepository.findByModuleCode(code)
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with code: " + code)));
    }



    public ModuleResponse updateModule(ModuleRequest moduleRequest, Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + id));
        module.setModuleName(moduleRequest.getModuleName());
        module.setModuleCode(moduleRequest.getModuleCode());
        module.setIcon(moduleRequest.getIcon());
        module.setUri(moduleRequest.getUri());
        module.setColor(moduleRequest.getColor());
        Module module1 = moduleRepository.save(module);
        return moduleMapper.DtoFromEntity(module1);
    }

    public void deleteModule(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + id));
        moduleRepository.delete(module);
    }

    public ModuleResponse enableModule(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + id));
        if (!module.getActif()) {
            module.setActif(true);
            moduleRepository.save(module);
            return moduleMapper.DtoFromEntity(module);
        } else
            throw new RessourceAlreadyEnabledException("Module with ID " + id + " is already enabled");

    }

    public ModuleResponse disableModule(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + id));
        if (module.getActif()) {
            module.setActif(false);
            moduleRepository.save(module);
            return moduleMapper.DtoFromEntity(module);
        } else
            throw new RessourceAlreadyDisabledException("Module with ID " + id + " is already disabled");
    }

    public List<ModuleResponse> getModulesExcludingModuleProfile(Long idProfile) {
        List<Module> modules = moduleRepository.findAll();
        List<Module> modulesProfile = profileRepository.findById(idProfile)
                .map(Profile::getModules)
                .orElse(Collections.emptyList());
        modules.removeAll(modulesProfile);
        return modules.stream().map(moduleMapper::DtoFromEntity).collect(Collectors.toList());
    }
    public List<ModuleResponse> getModulesByProfile(String username) {
        User user=userRepository.findByUserName(username).orElseThrow();
        Profile profile=user.getActifProfile();
        List<Module> modulesProfile = profile.getModules();
        return modulesProfile.stream().map(moduleMapper::DtoFromEntity).collect(Collectors.toList());
    }
    public Long count(){
        return moduleRepository.count();
    }
}
