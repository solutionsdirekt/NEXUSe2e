--
--  NEXUSe2e Business Messaging Open Source
--  direkt gruppe GmbH
--
--  This is free software; you can redistribute it and/or modify it
--  under the terms of the GNU Lesser General Public License as
--  published by the Free Software Foundation version 3 of
--  the License.
--
--  This software is distributed in the hope that it will be useful,
--  but WITHOUT ANY WARRANTY; without even the implied warranty of
--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
--  Lesser General Public License for more details.
--
--  You should have received a copy of the GNU Lesser General Public
--  License along with this software; if not, write to the Free
--  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
--  02110-1301 USA, or see the FSF site: http://www.fsf.org.
--
drop table nx_action cascade constraints;
drop table nx_certificate cascade constraints;
drop table nx_choreography cascade constraints;
drop table nx_component cascade constraints;
drop table nx_connection cascade constraints;
drop table nx_conversation cascade constraints;
drop table nx_follow_up_action cascade constraints;
drop table nx_generic_param cascade constraints;
drop table nx_grant cascade constraints;
drop table nx_log cascade constraints;
drop table nx_logger cascade constraints;
drop table nx_logger_param cascade constraints;
drop table nx_mapping cascade constraints;
drop table nx_message cascade constraints;
drop table nx_message_label cascade constraints;
drop table nx_message_payload cascade constraints;
drop table nx_participant cascade constraints;
drop table nx_partner cascade constraints;
drop table nx_pipelet cascade constraints;
drop table nx_pipelet_param cascade constraints;
drop table nx_pipeline cascade constraints;
drop table nx_role cascade constraints;
drop table nx_service cascade constraints;
drop table nx_service_param cascade constraints;
drop table nx_trp cascade constraints;
drop table nx_user cascade constraints;
drop sequence hibernate_sequence;
create table nx_action (nx_action_id integer not null, nx_choreography_id number(10,0) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, start_flag number(1,0) not null, end_flag number(1,0) not null, inbound_nx_pipeline_id number(10,0) not null, outbound_nx_pipeline_id number(10,0) not null, status_update_nx_pipeline_id number(10,0), name varchar2(64 char) not null, primary key (nx_action_id));
comment on table nx_action is '';
comment on column nx_action.created_date is '';
comment on column nx_action.modified_date is '';
comment on column nx_action.modified_nx_user_id is '';
comment on column nx_action.start_flag is '';
comment on column nx_action.end_flag is '';
comment on column nx_action.name is '';
create table nx_certificate (nx_certificate_id integer not null, type integer not null, password varchar2(64 char), created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, nx_partner_id number(10,0), name varchar2(512 char) not null, description varchar2(256 char), binary_data blob, primary key (nx_certificate_id));
comment on table nx_certificate is '';
comment on column nx_certificate.type is 'e.g. 1=Local Server Certificate, 2=PartnerCertificate, 3=CA Certificate, 4=Request';
comment on column nx_certificate.password is '';
comment on column nx_certificate.created_date is '';
comment on column nx_certificate.modified_date is '';
comment on column nx_certificate.modified_nx_user_id is '';
comment on column nx_certificate.name is '';
comment on column nx_certificate.description is '';
comment on column nx_certificate.binary_data is '';
create table nx_choreography (nx_choreography_id integer not null, description varchar2(255 char), name varchar2(255 char) not null unique, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, primary key (nx_choreography_id));
comment on table nx_choreography is '';
comment on column nx_choreography.description is '';
comment on column nx_choreography.name is '';
comment on column nx_choreography.created_date is '';
comment on column nx_choreography.modified_date is '';
comment on column nx_choreography.modified_nx_user_id is '';
create table nx_component (nx_component_id integer not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, type integer not null, name varchar2(64 char) not null, class_name varchar2(64 char) not null, description varchar2(64 char), primary key (nx_component_id));
comment on table nx_component is '';
comment on column nx_component.created_date is '';
comment on column nx_component.modified_date is '';
comment on column nx_component.modified_nx_user_id is '';
comment on column nx_component.type is '1=pipelet, 2=notifier';
comment on column nx_component.name is '';
comment on column nx_component.class_name is '';
comment on column nx_component.description is '';
create table nx_connection (nx_connection_id integer not null, nx_certificate_id number(10,0), nx_trp_id number(10,0) not null, nx_partner_id number(10,0) not null, timeout integer not null, message_interval integer not null, security_flag number(1,0) not null, reliable_flag number(1,0) not null, synchronous_flag number(1,0) not null, synchronous_timeout integer not null, retries integer not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, uri varchar2(512 char) not null, name varchar2(64 char) not null, login_name varchar2(64 char), password varchar2(64 char), description varchar2(64 char), primary key (nx_connection_id));
comment on table nx_connection is '';
comment on column nx_connection.timeout is '';
comment on column nx_connection.message_interval is '';
comment on column nx_connection.security_flag is '';
comment on column nx_connection.reliable_flag is '';
comment on column nx_connection.synchronous_timeout is '';
comment on column nx_connection.retries is '';
comment on column nx_connection.created_date is '';
comment on column nx_connection.modified_date is '';
comment on column nx_connection.modified_nx_user_id is '';
comment on column nx_connection.uri is '';
comment on column nx_connection.name is '';
comment on column nx_connection.login_name is '';
comment on column nx_connection.password is '';
comment on column nx_connection.description is '';
create table nx_conversation (nx_conversation_id integer not null, nx_choreography_id number(10,0) not null, nx_partner_id number(10,0) not null, conversation_id varchar2(96 char) not null, created_date timestamp not null, end_date timestamp, modified_date timestamp not null, modified_nx_user_id integer not null, status integer not null, message_count integer not null, current_nx_action_id number(10,0) not null, primary key (nx_conversation_id));
comment on table nx_conversation is '';
comment on column nx_conversation.conversation_id is 'Protocol specific conversation Id';
comment on column nx_conversation.created_date is '';
comment on column nx_conversation.end_date is '';
comment on column nx_conversation.modified_date is '';
comment on column nx_conversation.modified_nx_user_id is '';
comment on column nx_conversation.status is 'Message Status: e.g. processing, idle, awaiting_ack, error, complete';
create table nx_follow_up_action (nx_follow_up_action_id integer not null, nx_action_id number(10,0) not null, follow_up_nx_action_id number(10,0) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, primary key (nx_follow_up_action_id));
comment on table nx_follow_up_action is '';
comment on column nx_follow_up_action.created_date is '';
comment on column nx_follow_up_action.modified_date is '';
comment on column nx_follow_up_action.modified_nx_user_id is '';
create table nx_generic_param (nx_generic_param_id integer not null, category varchar2(128 char) not null, param_tag varchar2(128 char), created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, param_name varchar2(64 char) not null, param_label varchar2(64 char), param_value varchar2(1024 char), sequence_number integer, primary key (nx_generic_param_id));
comment on table nx_generic_param is '';
comment on column nx_generic_param.category is '';
comment on column nx_generic_param.param_tag is '';
comment on column nx_generic_param.created_date is '';
comment on column nx_generic_param.modified_date is '';
comment on column nx_generic_param.modified_nx_user_id is '';
comment on column nx_generic_param.param_name is '';
comment on column nx_generic_param.param_label is '';
comment on column nx_generic_param.param_value is '';
comment on column nx_generic_param.sequence_number is '';
create table nx_grant (nx_grant_id integer not null, target varchar2(64 char) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, nx_role_id number(10,0), nxGrantId number(10,0) not null, primary key (nx_grant_id));
comment on table nx_grant is '';
comment on column nx_grant.target is '';
comment on column nx_grant.created_date is '';
comment on column nx_grant.modified_date is '';
comment on column nx_grant.modified_nx_user_id is '';
create table nx_log (nx_log_id integer not null, log_id varchar2(255 char) not null, class_name varchar2(255 char) not null, method_name varchar2(255 char) not null, event_id integer not null, severity integer not null, conversation_id varchar2(96 char) not null, message_id varchar2(96 char) not null, description varchar2(255 char) not null, created_date timestamp not null, primary key (nx_log_id));
comment on table nx_log is '';
comment on column nx_log.log_id is 'might be used to separate different nexus processes';
comment on column nx_log.class_name is '';
comment on column nx_log.method_name is '';
comment on column nx_log.event_id is '';
comment on column nx_log.severity is '';
comment on column nx_log.conversation_id is '';
comment on column nx_log.message_id is '';
comment on column nx_log.description is '';
comment on column nx_log.created_date is '';
create table nx_logger (nx_logger_id number(10,0) not null, nx_component_id number(10,0) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, name varchar2(64 char) not null, autostart_flag number(1,0) not null, threshold integer not null, filter long not null, description varchar2(64 char), primary key (nx_logger_id));
comment on table nx_logger is '';
comment on column nx_logger.created_date is '';
comment on column nx_logger.modified_date is '';
comment on column nx_logger.modified_nx_user_id is '';
comment on column nx_logger.name is '';
comment on column nx_logger.autostart_flag is '';
comment on column nx_logger.threshold is '';
comment on column nx_logger.filter is '';
comment on column nx_logger.description is '';
create table nx_logger_param (nx_logger_param_id integer not null, nx_logger_id number(10,0) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, param_name varchar2(64 char) not null, param_label varchar2(64 char), param_value varchar2(1024 char), sequence_number integer, primary key (nx_logger_param_id));
comment on table nx_logger_param is '';
comment on column nx_logger_param.created_date is '';
comment on column nx_logger_param.modified_date is '';
comment on column nx_logger_param.modified_nx_user_id is '';
comment on column nx_logger_param.param_name is '';
comment on column nx_logger_param.param_label is '';
comment on column nx_logger_param.param_value is '';
comment on column nx_logger_param.sequence_number is '';
create table nx_mapping (nx_mapping_id integer not null, category varchar2(128 char) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, left_type integer not null, left_value varchar2(128 char), right_type integer not null, right_value varchar2(128 char), primary key (nx_mapping_id));
comment on table nx_mapping is '';
comment on column nx_mapping.category is '';
comment on column nx_mapping.created_date is '';
comment on column nx_mapping.modified_date is '';
comment on column nx_mapping.modified_nx_user_id is '';
comment on column nx_mapping.left_type is '';
comment on column nx_mapping.left_value is '';
comment on column nx_mapping.right_type is '';
comment on column nx_mapping.right_value is '';
create table nx_message (nx_message_id integer not null, nx_conversation_id number(10,0) not null, message_id varchar2(96 char) not null, header_data blob, type integer not null, nx_action_id number(10,0) not null, status integer not null, nx_trp_id number(10,0), referenced_nx_message_id number(10,0), retries integer not null, direction_flag number(1,0) not null, expiration_date timestamp, end_date timestamp, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, primary key (nx_message_id));
comment on table nx_message is '';
comment on column nx_message.message_id is 'Protocol specific Message Id';
comment on column nx_message.header_data is '';
comment on column nx_message.type is 'Message Type: e.g. 1=Normal, 2=Ack, 3=Error (Defined in messaging.Constants)';
comment on column nx_message.status is 'Message Status: e.g. queued, stopped, failed, retrying, send';
comment on column nx_message.retries is '';
comment on column nx_message.direction_flag is 'outbound is true, inbound is false';
comment on column nx_message.expiration_date is '';
comment on column nx_message.end_date is '';
comment on column nx_message.created_date is '';
comment on column nx_message.modified_date is '';
comment on column nx_message.modified_nx_user_id is '';
create table nx_message_label (nx_message_label_id integer not null, nx_message_id number(10,0) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, message_label varchar2(64 char), message_label_value varchar2(512 char) not null, primary key (nx_message_label_id));
comment on table nx_message_label is '';
comment on column nx_message_label.created_date is '';
comment on column nx_message_label.modified_date is '';
comment on column nx_message_label.modified_nx_user_id is '';
comment on column nx_message_label.message_label is '';
comment on column nx_message_label.message_label_value is '';
create table nx_message_payload (nx_message_payload_id integer not null, nx_message_id number(10,0) not null, sequence_number integer not null, mime_type varchar2(64 char) not null, content_id varchar2(96 char) not null, payload_data blob not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, primary key (nx_message_payload_id));
comment on table nx_message_payload is '';
comment on column nx_message_payload.sequence_number is '';
comment on column nx_message_payload.mime_type is '';
comment on column nx_message_payload.content_id is '';
comment on column nx_message_payload.payload_data is '';
comment on column nx_message_payload.created_date is '';
comment on column nx_message_payload.modified_date is '';
comment on column nx_message_payload.modified_nx_user_id is '';
create table nx_participant (nx_participant_id integer not null, nx_local_certificate_id number(10,0), nx_partner_id number(10,0) not null, nx_choreography_id number(10,0) not null, nx_local_partner_id number(10,0) not null, nx_connection_id number(10,0) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, description varchar2(64 char), primary key (nx_participant_id));
comment on table nx_participant is '';
comment on column nx_participant.created_date is '';
comment on column nx_participant.modified_date is '';
comment on column nx_participant.modified_nx_user_id is '';
comment on column nx_participant.description is '';
create table nx_partner (nx_partner_id integer not null, type integer not null, company_name varchar2(64 char), address_line_1 varchar2(64 char), address_line_2 varchar2(64 char), city varchar2(64 char), state varchar2(64 char), zip varchar2(64 char), country varchar2(64 char), created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, name varchar2(64 char), description varchar2(64 char), partner_id varchar2(64 char) not null, partner_id_type varchar2(64 char) not null, primary key (nx_partner_id), unique (type, partner_id));
comment on table nx_partner is '';
comment on column nx_partner.type is 'e.g Localpartner information or Businesspartner';
comment on column nx_partner.company_name is '';
comment on column nx_partner.address_line_1 is '';
comment on column nx_partner.address_line_2 is '';
comment on column nx_partner.city is '';
comment on column nx_partner.state is '';
comment on column nx_partner.zip is '';
comment on column nx_partner.country is '';
comment on column nx_partner.created_date is '';
comment on column nx_partner.modified_date is '';
comment on column nx_partner.modified_nx_user_id is '';
comment on column nx_partner.name is '';
comment on column nx_partner.description is '';
comment on column nx_partner.partner_id is '';
comment on column nx_partner.partner_id_type is '';
create table nx_pipelet (nx_pipelet_id number(10,0) not null, nx_pipeline_id number(10,0) not null, nx_component_id number(10,0) not null, frontend_flag number(1,0) not null, forward_flag number(1,0) not null, endpoint_flag number(1,0) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, position integer not null, name varchar2(64 char) not null, description varchar2(64 char), primary key (nx_pipelet_id));
comment on table nx_pipelet is '';
comment on column nx_pipelet.frontend_flag is 'frontend is true, backend is false';
comment on column nx_pipelet.forward_flag is 'forward is true, return is false';
comment on column nx_pipelet.endpoint_flag is 'endpoint is true, pipelet in pipeline is false';
comment on column nx_pipelet.created_date is '';
comment on column nx_pipelet.modified_date is '';
comment on column nx_pipelet.modified_nx_user_id is '';
comment on column nx_pipelet.position is 'position in pipeline. Starting with 0';
comment on column nx_pipelet.name is '';
comment on column nx_pipelet.description is '';
create table nx_pipelet_param (nx_pipelet_param_id integer not null, nx_pipelet_id number(10,0) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, param_name varchar2(64 char) not null, param_label varchar2(64 char), param_value varchar2(1024 char), sequence_number integer, primary key (nx_pipelet_param_id));
comment on table nx_pipelet_param is '';
comment on column nx_pipelet_param.created_date is '';
comment on column nx_pipelet_param.modified_date is '';
comment on column nx_pipelet_param.modified_nx_user_id is '';
comment on column nx_pipelet_param.param_name is '';
comment on column nx_pipelet_param.param_label is '';
comment on column nx_pipelet_param.param_value is '';
comment on column nx_pipelet_param.sequence_number is '';
create table nx_pipeline (nx_pipeline_id integer not null, direction_flag number(1,0) not null, frontend_flag number(1,0) not null, nx_trp_id number(10,0), created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, description varchar2(64 char), name varchar2(64 char) not null, primary key (nx_pipeline_id));
comment on table nx_pipeline is '';
comment on column nx_pipeline.direction_flag is '';
comment on column nx_pipeline.frontend_flag is 'frontend is true, backend is false';
comment on column nx_pipeline.created_date is '';
comment on column nx_pipeline.modified_date is '';
comment on column nx_pipeline.modified_nx_user_id is '';
comment on column nx_pipeline.description is '';
comment on column nx_pipeline.name is '';
create table nx_role (nx_role_id integer not null, name varchar2(64 char) not null, description varchar2(64 char), created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, primary key (nx_role_id));
comment on table nx_role is '';
comment on column nx_role.name is '';
comment on column nx_role.description is '';
comment on column nx_role.created_date is '';
comment on column nx_role.modified_date is '';
comment on column nx_role.modified_nx_user_id is '';
create table nx_service (nx_service_id number(10,0) not null, nx_component_id number(10,0) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, position integer not null, autostart_flag number(1,0) not null, name varchar2(64 char) not null, description varchar2(64 char), primary key (nx_service_id));
comment on table nx_service is '';
comment on column nx_service.created_date is '';
comment on column nx_service.modified_date is '';
comment on column nx_service.modified_nx_user_id is '';
comment on column nx_service.position is 'Service position, starting with 0';
comment on column nx_service.autostart_flag is '';
comment on column nx_service.name is '';
comment on column nx_service.description is '';
create table nx_service_param (nx_service_param_id integer not null, nx_service_id number(10,0) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, param_name varchar2(64 char) not null, param_label varchar2(64 char), param_value varchar2(1024 char), sequence_number integer, primary key (nx_service_param_id));
comment on table nx_service_param is '';
comment on column nx_service_param.created_date is '';
comment on column nx_service_param.modified_date is '';
comment on column nx_service_param.modified_nx_user_id is '';
comment on column nx_service_param.param_name is '';
comment on column nx_service_param.param_label is '';
comment on column nx_service_param.param_value is '';
comment on column nx_service_param.sequence_number is '';
create table nx_trp (nx_trp_id integer not null, protocol varchar2(64 char) not null, version varchar2(64 char) not null, transport varchar2(64 char) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, primary key (nx_trp_id));
comment on table nx_trp is '';
comment on column nx_trp.protocol is '';
comment on column nx_trp.version is '';
comment on column nx_trp.transport is '';
comment on column nx_trp.created_date is '';
comment on column nx_trp.modified_date is '';
comment on column nx_trp.modified_nx_user_id is '';
create table nx_user (nx_user_id integer not null, login_name varchar2(64 char) not null, first_name varchar2(64 char) not null, middle_name varchar2(64 char), last_name varchar2(64 char) not null, password varchar2(64 char) not null, created_date timestamp not null, modified_date timestamp not null, modified_nx_user_id integer not null, active_flag number(1,0) not null, visible_flag number(1,0) not null, nx_role_id number(10,0), primary key (nx_user_id));
comment on table nx_user is '';
comment on column nx_user.login_name is '';
comment on column nx_user.first_name is '';
comment on column nx_user.middle_name is '';
comment on column nx_user.last_name is '';
comment on column nx_user.password is '';
comment on column nx_user.created_date is '';
comment on column nx_user.modified_date is '';
comment on column nx_user.modified_nx_user_id is '';
comment on column nx_user.active_flag is '';
comment on column nx_user.visible_flag is '';
create index ix_action_3 on nx_action (outbound_nx_pipeline_id);
create index ix_action_1 on nx_action (nx_choreography_id);
create index ix_action_2 on nx_action (inbound_nx_pipeline_id);
create index ix_action_4 on nx_action (status_update_nx_pipeline_id);
alter table nx_action add constraint fk__action__status_update_pipeline_id foreign key (status_update_nx_pipeline_id) references nx_pipeline;
alter table nx_action add constraint fk__action__inbound_pipeline_id foreign key (inbound_nx_pipeline_id) references nx_pipeline;
alter table nx_action add constraint fk__action__outbound_pipeline_id foreign key (outbound_nx_pipeline_id) references nx_pipeline;
alter table nx_action add constraint fk__action__choreography_id foreign key (nx_choreography_id) references nx_choreography;
create index ix_certificate_1 on nx_certificate (nx_partner_id);
alter table nx_certificate add constraint fk__certificate__partner_id foreign key (nx_partner_id) references nx_partner;
create index ix_connection_3 on nx_connection (nx_partner_id);
create index ix_connection_2 on nx_connection (nx_trp_id);
create index ix_connection_1 on nx_connection (nx_certificate_id);
alter table nx_connection add constraint fk__connection__certificate_id foreign key (nx_certificate_id) references nx_certificate;
alter table nx_connection add constraint fk__connection__partner_id foreign key (nx_partner_id) references nx_partner;
alter table nx_connection add constraint fk__connection__trp_id foreign key (nx_trp_id) references nx_trp;
create index ix_conversation_3 on nx_conversation (current_nx_action_id);
create index ix_conversation_2 on nx_conversation (nx_partner_id);
create index ix_conversation_1 on nx_conversation (nx_choreography_id);
alter table nx_conversation add constraint fk__conversation__current_action_id foreign key (current_nx_action_id) references nx_action;
alter table nx_conversation add constraint fk__conversation__partner_id foreign key (nx_partner_id) references nx_partner;
alter table nx_conversation add constraint fk__conversation__choreography_id foreign key (nx_choreography_id) references nx_choreography;
create index ix_follow_up_action_2 on nx_follow_up_action (follow_up_nx_action_id);
create index ix_follow_up_action_1 on nx_follow_up_action (nx_action_id);
alter table nx_follow_up_action add constraint fk__follow_up_action__follow_up_action_id foreign key (follow_up_nx_action_id) references nx_action;
alter table nx_follow_up_action add constraint fk__follow_up_action__action_id foreign key (nx_action_id) references nx_action;
create index ix_grant_1 on nx_grant (nx_role_id);
alter table nx_grant add constraint fk__grant__grant_id foreign key (nxGrantId) references nx_role;
alter table nx_grant add constraint fk__grant__role_id foreign key (nx_role_id) references nx_role;
create index ix_logger_1 on nx_logger (nx_component_id);
alter table nx_logger add constraint fk__logger__component_id foreign key (nx_component_id) references nx_component;
alter table nx_logger_param add constraint fk__logger_param__logger_id foreign key (nx_logger_id) references nx_logger;
create index ix_message_1 on nx_message (nx_conversation_id);
create index ix_message_3 on nx_message (nx_trp_id);
create index ix_message_4 on nx_message (referenced_nx_message_id);
create index ix_message_5 on nx_message (status);
create index ix_message_2 on nx_message (nx_action_id);
alter table nx_message add constraint fk__message__conversation_id foreign key (nx_conversation_id) references nx_conversation;
alter table nx_message add constraint fk__message__trp_id foreign key (nx_trp_id) references nx_trp;
alter table nx_message add constraint fk__message__action_id foreign key (nx_action_id) references nx_action;
alter table nx_message add constraint fk__message__referenced_message_id foreign key (referenced_nx_message_id) references nx_message;
create index ix_message_label_1 on nx_message_label (nx_message_id);
alter table nx_message_label add constraint fk__message_label__message_id foreign key (nx_message_id) references nx_message;
create index ix_message_payload_1 on nx_message_payload (nx_message_id);
alter table nx_message_payload add constraint fk__message_payload__message_id foreign key (nx_message_id) references nx_message;
create index ix_participant_5 on nx_participant (nx_connection_id);
create index ix_participant_2 on nx_participant (nx_partner_id);
create index ix_participant_4 on nx_participant (nx_local_partner_id);
create index ix_participant_1 on nx_participant (nx_local_certificate_id);
create index ix_participant_3 on nx_participant (nx_choreography_id);
alter table nx_participant add constraint fk__participant__connection_id foreign key (nx_connection_id) references nx_connection;
alter table nx_participant add constraint fk__participant__partner_id foreign key (nx_partner_id) references nx_partner;
alter table nx_participant add constraint fk__participant__certificate_id foreign key (nx_local_certificate_id) references nx_certificate;
alter table nx_participant add constraint fk__participant__choreography_id foreign key (nx_choreography_id) references nx_choreography;
alter table nx_participant add constraint fk__participant__local_partner_id foreign key (nx_local_partner_id) references nx_partner;
create index ix_pipelet_1 on nx_pipelet (nx_pipeline_id);
create index ix_pipelet_2 on nx_pipelet (nx_component_id);
alter table nx_pipelet add constraint fk__pipelet__component_id foreign key (nx_component_id) references nx_component;
alter table nx_pipelet add constraint fk__pipelet__pipeline_id foreign key (nx_pipeline_id) references nx_pipeline;
create index ix_pipelet_param_1 on nx_pipelet_param (nx_pipelet_id);
alter table nx_pipelet_param add constraint fk__pipelet_param__pipelet_id foreign key (nx_pipelet_id) references nx_pipelet;
create index ix_pipeline_1 on nx_pipeline (nx_trp_id);
alter table nx_pipeline add constraint fk__pipeline__trp_id foreign key (nx_trp_id) references nx_trp;
create index ix_service_1 on nx_service (nx_component_id);
alter table nx_service add constraint fk__service__component_id foreign key (nx_component_id) references nx_component;
create index ix_service_param_1 on nx_service_param (nx_service_id);
alter table nx_service_param add constraint fk__service_param__service_id foreign key (nx_service_id) references nx_service;
create index ix_user_1 on nx_user (nx_role_id);
alter table nx_user add constraint fk__user__role_id foreign key (nx_role_id) references nx_role;
create sequence hibernate_sequence;
