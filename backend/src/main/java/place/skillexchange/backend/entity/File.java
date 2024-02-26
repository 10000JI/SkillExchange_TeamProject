package place.skillexchange.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.file.UploadFile;

import java.sql.Blob;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "ori_name")
    private String oriName; //클라이언트가 업로드한 파일명

//    @Column(name = "file_name")
//    private String fileName; //서버 내부에서 관리하는 파일명

    @Column(name = "file_url")
    private String fileUrl; //서버 내부에서 관리하는 파일명


    /**
     * 프로필 이미지 수정
     */
    public void changeProfileImg(UploadFile uploadFile) {
        this.oriName = uploadFile.getUploadFileName();
        this.fileUrl = uploadFile.getFileUrl();
    }

}
