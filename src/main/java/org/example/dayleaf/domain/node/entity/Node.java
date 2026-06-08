package org.example.dayleaf.domain.node.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dayleaf.global.base.BaseEntity;

@Getter
@Entity
@Table(name = "node")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Node extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Node parent;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NodeType type;

    @Column(nullable = false)
    private int depth;

    private String path;

    @Column(nullable = false)
    private int position;

    @Column(name = "is_archived", nullable = false)
    private boolean archived;

    @Builder
    private Node(Node parent, Long memberId, String title, NodeType type, int depth, String path, int position,
                boolean archived) {
        this.parent = parent;
        this.memberId = memberId;
        this.title = title;
        this.type = type;
        this.depth = depth;
        this.path = path;
        this.position = position;
        this.archived = archived;
    }
}
