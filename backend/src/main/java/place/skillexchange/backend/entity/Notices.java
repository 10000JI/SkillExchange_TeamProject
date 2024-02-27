package place.skillexchange.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Notices extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "board_title", length = 50, nullable = false)
    private String title;

    @Column(name = "board_content", length = 4000, nullable = false)
    private String content;

    @Column(name = "board_hit")
    @ColumnDefault("0")
    private Long hit;
}
