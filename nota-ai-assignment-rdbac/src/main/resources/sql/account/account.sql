CREATE TABLE account
(
    id         BIGINT AUTO_INCREMENT       NOT NULL COMMENT '인덱싱 컬럼',
    tenant_key VARCHAR(100)                NOT NULL COMMENT '테넌트 키',
    email      VARCHAR(100)                NOT NULL COMMENT '로그인 이메일',
    password   VARCHAR(100)                NOT NULL COMMENT '해싱한 비밀번호',
    role       VARCHAR(100)                NOT NULL COMMENT '권한',
    status     ENUM ('ACTIVE', 'INACTIVE') NOT NULL COMMENT '상태',
    created_at DATETIME                    NOT NULL COMMENT '생성일',
    deleted_at DATETIME                    NULL COMMENT '삭제일',
    PRIMARY KEY (id),
    UNIQUE KEY uc_account_tenant_email (email, tenant_key)
)
    COMMENT ='계정'
    COLLATE = UTF8MB4_UNICODE_CI;

