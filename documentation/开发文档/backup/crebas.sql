/*==============================================================*/
/* DBMS name:      MySQL 4.0                                    */
/* Created on:     2007-6-28 23:58:37                           */
/*==============================================================*/


drop index index_agent on user_agent;

drop index index_user_org_priority on user_org;

drop index index_user_parent_org on user_org_subjection;

drop index index_person_loginName on user_person;

drop index index_person_name on user_person;

drop index index_user_person_job on user_person_job;

drop index index_user_person_subjection on user_person_subjection;

drop index index_user_regist_permit on user_regist_permit;

drop index index_user_family_genearch on user_student_genearch;

drop index index_user_family_student on user_student_genearch;

drop index index_view_custom_userId on view_custom;

drop index index_view_custom_viewname on view_custom;

drop table if exists eai_configure;

drop table if exists logger_action_log;

drop table if exists user_agent;

drop table if exists user_area;

drop table if exists user_class;

drop table if exists user_class_teacher;

drop table if exists user_department;

drop table if exists user_employee;

drop table if exists user_genearch;

drop table if exists user_job;

drop table if exists user_org;

drop table if exists user_org_subjection;

drop table if exists user_person;

drop table if exists user_person_job;

drop table if exists user_person_subjection;

drop table if exists user_regist_permit;

drop table if exists user_role;

drop table if exists user_role_member;

drop table if exists user_root;

drop table if exists user_school;

drop table if exists user_school_category;

drop table if exists user_school_department;

drop table if exists user_school_job;

drop table if exists user_student;

drop table if exists user_student_genearch;

drop table if exists user_teacher;

drop table if exists user_unit;

drop table if exists view_custom;

/*==============================================================*/
/* Table: eai_configure                                         */
/*==============================================================*/
create table eai_configure
(
   id                             numeric(30,0)                  not null,
   configure                      mediumtext,
   configureExtend                mediumtext,
   primary key (id)
)
comment = "EAI配置";

/*==============================================================*/
/* Table: logger_action_log                                     */
/*==============================================================*/
create table logger_action_log
(
   id                             numeric(30,0)                  not null,
   applicationName                varchar(20),
   recordType                     varchar(200),
   recordId                       numeric(30,0),
   content                        varchar(250),
   personId                       numeric(30,0),
   personName                     varchar(20),
   actionName                     varchar(200),
   actionTime                     timestamp,
   actionType                     varchar(20),
   primary key (id)
)
comment = "日志:操作日志";

/*==============================================================*/
/* Table: user_agent                                            */
/*==============================================================*/
create table user_agent
(
   id                             numeric(30,0)                  not null,
   personId                       numeric(30,0),
   agentId                        numeric(30,0),
   beginTime                      timestamp,
   endTime                        timestamp,
   source                         varchar(50),
   primary key (id)
)
comment = "用户：代理人";

/*==============================================================*/
/* Index: index_agent                                           */
/*==============================================================*/
create index index_agent on user_agent
(
   personId,
   agentId,
   beginTime,
   endTime
);

/*==============================================================*/
/* Table: user_area                                             */
/*==============================================================*/
create table user_area
(
   id                             numeric(30,0)                  not null,
   primary key (id)
)
comment = "组织机构：区域,如福建";

/*==============================================================*/
/* Table: user_class                                            */
/*==============================================================*/
create table user_class
(
   id                             numeric(30,0)                  not null,
   enrollTime                     numeric(4,0),
   lengthOfSchooling              numeric(2,0),
   classNumber                    numeric(3,0),
   primary key (id)
);

/*==============================================================*/
/* Table: user_class_teacher                                    */
/*==============================================================*/
create table user_class_teacher
(
   id                             numeric(30,0)                  not null,
   classId                        numeric(30,0),
   title                          varchar(30),
   teacherId                      numeric(30,0),
   primary key (id)
);

/*==============================================================*/
/* Table: user_department                                       */
/*==============================================================*/
create table user_department
(
   id                             numeric(30,0)                  not null,
   primary key (id)
)
comment = "组织机构：部门,暂不使用";

/*==============================================================*/
/* Table: user_employee                                         */
/*==============================================================*/
create table user_employee
(
   id                             numeric(30,0)                  not null,
   primary key (id)
)
comment = "用户:普通用户";

insert into user_employee values (5001);
/*==============================================================*/
/* Table: user_genearch                                         */
/*==============================================================*/
create table user_genearch
(
   id                             numeric(30,0)                  not null,
   primary key (id)
)
comment = "用户:家长";

/*==============================================================*/
/* Table: user_job                                              */
/*==============================================================*/
create table user_job
(
   id                             numeric(30,0),
   orgId                          numeric(30,0),
   jobName                        varchar(30),
   priority                       numeric(4,0)
);

/*==============================================================*/
/* Table: user_org                                              */
/*==============================================================*/
create table user_org
(
   id                             numeric(30,0)                  not null,
   sid                            numeric(10,0),
   name                           varchar(50),
   fullName                       varchar(120),
   parentOrgId                    numeric(30,0),
   type                           numeric(1,0),
   creator                        varchar(50),
   creatorId                      numeric(30,0),
   created                        timestamp,
   priority                       numeric(6,4),
   remark                         varchar(250),
   primary key (id)
)
comment = "组织机构";

/*==============================================================*/
/* Index: index_user_org_priority                               */
/*==============================================================*/
create index index_user_org_priority on user_org
(
   name,
   priority
);

/*==============================================================*/
/* Table: user_org_subjection                                   */
/*==============================================================*/
create table user_org_subjection
(
   id                             numeric(30,0)                  not null,
   orgId                          numeric(30,0),
   parentOrgId                    numeric(30,0),
   primary key (id)
)
comment = "组织机构：所属组织";

/*==============================================================*/
/* Index: index_user_parent_org                                 */
/*==============================================================*/
create unique index index_user_parent_org on user_org_subjection
(
   orgId,
   parentOrgId
);

/*==============================================================*/
/* Table: user_person                                           */
/*==============================================================*/
create table user_person
(
   id                             numeric(30,0)                  not null,
   sid                            numeric(10,0),
   name                           varchar(30),
   loginName                      varchar(30),
   sex                            numeric(1,0),
   type                           numeric(1,0),
   password                       varchar(100),
   halt                           CHAR(1),
   priority                       numeric(4,0),
   creator                        varchar(50),
   creatorId                      numeric(30,0),
   created                        timestamp,
   mailAddress                    varchar(100),
   tel                            varchar(16),
   telFamily                      varchar(16),
   mobile                         varchar(16),
   familyAddress                  varchar(100),
   lastSetPassword                timestamp,
   deleteDisable                  CHAR(1),
   preassign                      CHAR(1),
   remark                         varchar(250),
   primary key (id)
)
comment = "用户";

insert into user_person (id, name, loginName, type, password, halt, priority, creatorId, deleteDisable, preassign, sid, sex)
 values	(5001, 'IM Admin', 'admin', 1, 'loNwxfy8ZZ41EdxiuEJPbw==', 0, 0, 0, 1, 1, 999, 0);
/*==============================================================*/
/* Index: index_person_name                                     */
/*==============================================================*/
create index index_person_name on user_person
(
   name
);

/*==============================================================*/
/* Index: index_person_loginName                                */
/*==============================================================*/
create unique index index_person_loginName on user_person
(
   loginName
);

/*==============================================================*/
/* Table: user_person_job                                       */
/*==============================================================*/
create table user_person_job
(
   id                             numeric(30,0)                  not null,
   personId                       numeric(30,0),
   jobId                          numeric(30,0),
   primary key (id)
)
comment = "用户：用户所处岗位/职务，可以是多个，第一个为主要岗位";

/*==============================================================*/
/* Index: index_user_person_job                                 */
/*==============================================================*/
create unique index index_user_person_job on user_person_job
(
   personId,
   jobId
);

/*==============================================================*/
/* Table: user_person_subjection                                */
/*==============================================================*/
create table user_person_subjection
(
   id                             numeric(30,0)                  not null,
   personId                       numeric(30,0),
   orgId                          numeric(30,0),
   primary key (id)
)
comment = "用户：用户所在机构，可以是多个，第一个为主要机构";

insert into user_person_subjection (id, personId, orgId) values (5001, 5001, 0);
/*==============================================================*/
/* Index: index_user_person_subjection                          */
/*==============================================================*/
create unique index index_user_person_subjection on user_person_subjection
(
   personId,
   orgId
);

/*==============================================================*/
/* Table: user_regist_permit                                    */
/*==============================================================*/
create table user_regist_permit
(
   id                             numeric(30,0)                  not null,
   orgId                          numeric(30,0),
   userId                         numeric(30,0),
   userName                       varchar(50),
   fullControl                    CHAR(1),
   registStudent                  CHAR(1),
   registTeacher                  CHAR(1),
   registEmployee                 CHAR(1),
   registChildOrg                 CHAR(1),
   authorizable                   CHAR(1),
   creator                        varchar(50),
   creatorId                      numeric(30,0),
   created                        timestamp,
   primary key (id)
);

/*==============================================================*/
/* Index: index_user_regist_permit                              */
/*==============================================================*/
create unique index index_user_regist_permit on user_regist_permit
(
   orgId,
   userId
);

/*==============================================================*/
/* Table: user_role                                             */
/*==============================================================*/
create table user_role
(
   id                             numeric(30,0),
   orgId                          numeric(30,0),
   roleName                       varchar(30)
);

/*==============================================================*/
/* Table: user_role_member                                      */
/*==============================================================*/
create table user_role_member
(
   id                             numeric(30,0)                  not null,
   roleId                         numeric(30,0),
   memberId                       numeric(30,0),
   primary key (id)
);

/*==============================================================*/
/* Table: user_root                                             */
/*==============================================================*/
create table user_root
(
   id                             numeric(30,0)                  not null,
   agnomen                        varchar(50),
   primary key (id)
)
comment = "组织机构：顶层组织";

/*==============================================================*/
/* Table: user_school                                           */
/*==============================================================*/
create table user_school
(
   id                             numeric(30,0)                  not null,
   category                       varchar(20),
   primary key (id)
);

/*==============================================================*/
/* Table: user_school_category                                  */
/*==============================================================*/
create table user_school_category
(
   id                             numeric(30,0)                  not null,
   category                       varchar(30),
   primary key (id)
)
comment = "如小学、中学";

/*==============================================================*/
/* Table: user_school_department                                */
/*==============================================================*/
create table user_school_department
(
   id                             numeric(30,0)                  not null,
   primary key (id)
)
comment = "大部分学校都有的部门，如语文教研组、数学教研组等";

/*==============================================================*/
/* Table: user_school_job                                       */
/*==============================================================*/
create table user_school_job
(
   id                             numeric(30,0)                  not null,
   jobName                        varchar(20),
   priority                       numeric(4,0),
   primary key (id)
)
comment = "大部分学校都会有的岗位，如校长、副校长、教研组长等";

/*==============================================================*/
/* Table: user_student                                          */
/*==============================================================*/
create table user_student
(
   id                             numeric(30,0)                  not null,
   seatNumber                     numeric(3,0),
   primary key (id)
)
comment = "用户:学生";

/*==============================================================*/
/* Table: user_student_genearch                                 */
/*==============================================================*/
create table user_student_genearch
(
   id                             numeric(30,0)                  not null,
   studentId                      numeric(30,0),
   genearchId                     numeric(30,0),
   relation                       varchar(20),
   primary key (id)
)
comment = "用户：学生家长";

/*==============================================================*/
/* Index: index_user_family_student                             */
/*==============================================================*/
create index index_user_family_student on user_student_genearch
(
   studentId
);

/*==============================================================*/
/* Index: index_user_family_genearch                            */
/*==============================================================*/
create index index_user_family_genearch on user_student_genearch
(
   genearchId
);

/*==============================================================*/
/* Table: user_teacher                                          */
/*==============================================================*/
create table user_teacher
(
   id                             numeric(30,0)                  not null,
   primary key (id)
)
comment = "用户:教师";

/*==============================================================*/
/* Table: user_unit                                             */
/*==============================================================*/
create table user_unit
(
   id                             numeric(30,0)                  not null,
   primary key (id)
);

/*==============================================================*/
/* Table: view_custom                                           */
/*==============================================================*/
create table view_custom
(
   id                             numeric(30,0)                  not null,
   userId                         numeric(30,0),
   userType                       CHAR(1),
   applicationName                varchar(20),
   viewName                       varchar(20),
   custom                         longblob,
   primary key (id)
)
comment = "视图定制";

/*==============================================================*/
/* Index: index_view_custom_userId                              */
/*==============================================================*/
create index index_view_custom_userId on view_custom
(
   userId
);

/*==============================================================*/
/* Index: index_view_custom_viewname                            */
/*==============================================================*/
create index index_view_custom_viewname on view_custom
(
   applicationName,
   viewName
);

