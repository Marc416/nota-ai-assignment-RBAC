CREATE TABLE project
(
    id         BIGINT AUTO_INCREMENT      NOT NULL COMMENT '인덱싱 컬럼',
    title      VARCHAR(100)               NOT NULL COMMENT '제목',
    owner_id   BIGINT                     NOT NULL COMMENT '프로젝트 생성자 accountId',
    status     ENUM ('ACTIVE', 'DELETED') NOT NULL COMMENT '상태',
    created_at DATETIME                   NOT NULL COMMENT '생성일',
    updated_at DATETIME                   NULL COMMENT '수정일',
    deleted_at DATETIME                   NULL COMMENT '삭제일',
    PRIMARY KEY (id)
)
    COMMENT ='프로젝트'
    COLLATE = UTF8MB4_UNICODE_CI;


CREATE TABLE project_member
(
    id         BIGINT AUTO_INCREMENT                      NOT NULL COMMENT '인덱싱 컬럼',
    account_id BIGINT                                     NOT NULL COMMENT '멤버 Id',
    project_id BIGINT                                     NOT NULL COMMENT '프로젝트 Id',
    role       ENUM ('EDITOR', 'VIEWER', 'PROJECT_OWNER') NOT NULL COMMENT '멤버 역할',
    created_at DATETIME                                   NOT NULL COMMENT '생성일',
    deleted_at DATETIME                                   NULL COMMENT '삭제일',
    PRIMARY KEY (id),
    UNIQUE KEY uc_project_member_account_project (account_id, project_id)
)
    COMMENT ='프로젝트 참가 멤버'
    COLLATE = UTF8MB4_UNICODE_CI;

