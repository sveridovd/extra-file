package pro.atomicity.extrafile;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class MultipartFileWrapper implements MultipartFile {

    private String fileName;

    private File file;

    private String mimeType;

    public MultipartFileWrapper(String path) {
        this(Paths.get(
                Objects.requireNonNull(path)
        ));
    }

    public MultipartFileWrapper(Path path) {
        this(Objects.requireNonNull(path).toFile());
    }

    public MultipartFileWrapper(File file) {
        this.file = Objects.requireNonNull(file);
        fileName = file.getName();

        if (StringUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("File name empty");
        }

        if (file.isDirectory()) {
            throw new IllegalArgumentException("File is directory");
        }
    }

    public String getName() {
        return fileName;
    }

    public String getOriginalFilename() {
        return fileName;
    }

    public String getContentType() {
        if (mimeType == null) {
            try {
                mimeType = Files.probeContentType(file.toPath());
            } catch (IOException ex) {
                throw new RuntimeException("probeContentType failed", ex);
            }
        }
        return mimeType;
    }

    public boolean isEmpty() {
        return false;
    }

    public long getSize() {
        return file.length();
    }

    public byte[] getBytes() throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    public void transferTo(File target) throws IOException, IllegalStateException {
        Files.move(file.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
