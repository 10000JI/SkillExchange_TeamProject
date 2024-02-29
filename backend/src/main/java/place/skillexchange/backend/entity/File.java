package place.skillexchange.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import place.skillexchange.backend.file.UploadFile;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id", nullable = false)
    private Long id;

    @Column(name = "ori_name", nullable = false)
    private String oriName; //클라이언트가 업로드한 파일명

    @Column(name = "file_url", nullable = false)
    private String fileUrl; //서버 내부에서 관리하는 파일명

    /**
     * User와 File은 1:1 관계
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    /**
     * 양방향 매핑
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Notice notice;


    /**
     * 프로필 이미지 수정
     */
    public void changeProfileImg(UploadFile uploadFile) {
        this.oriName = uploadFile.getUploadFileName();
        this.fileUrl = uploadFile.getFileUrl();
    }

}
