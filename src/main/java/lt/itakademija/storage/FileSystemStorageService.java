package lt.itakademija.storage;

import lt.itakademija.electors.candidate.CandidateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    @Autowired
    CSVParser reader;

    @Value("${storage.uploadPath}")
    private Path uploadPath;

    @Override
    public List<CandidateEntity> store(String partyOrCounty, MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            reader.setPartyOrCounty(partyOrCounty);
            return reader.extractCandidates(file.getInputStream());

//            Files.copy(file.getInputStream(), this.uploadPath.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.uploadPath, 1)
                    .filter(path -> !path.equals(this.uploadPath))
                    .map(path -> this.uploadPath.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to extractCandidates stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return uploadPath.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not extractCandidates file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not extractCandidates file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(uploadPath.toFile());
    }

    @Override
    public void init() {
    }
}

@ConfigurationProperties("storage")
class StorageProperties {

    private String location = "uploaded-files";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}