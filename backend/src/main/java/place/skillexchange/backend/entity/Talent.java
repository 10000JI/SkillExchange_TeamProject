package place.skillexchange.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Talent extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "talent_id")
    private Long id;

    /**
     * 단방향 매핑 (단뱡향일 때는 Cascade 작동 X, User 삭제 시 Talent 삭제 후 User 삭제 해야 함)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="writer")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    /**
     * 단방향 매핑
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teached_subject_id", nullable = false)
    private SubjectCategory teachedSubject;

    /**
     * 단방향 매핑
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teaching_subject_id", nullable = false)
    private SubjectCategory teachingSubject;

    @Column(name = "talent_content", length = 4000, nullable = false)
    private String content;

    @Column(name = "talent_hit")
    @ColumnDefault("0")
    private Long hit;

    @Column(name = "age_group", length = 50, nullable = false)
    private String ageGroup;

    @Column(name = "talent_week", length = 50, nullable = false)
    private String week;

    /**
     * 이미지와 양방향 매핑
     */
    @OneToMany(mappedBy = "talent", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

}

