-- タスクの識別子
CREATE SEQUENCE IF NOT EXISTS TASK_SEQ;

-- 1レコードがToDoリストのタスクに相当
CREATE TABLE IF NOT EXISTS task (
   id INTEGER DEFAULT NEXTVAL('TASK_SEQ') PRIMARY KEY, -- 識別子
   `user` VARCHAR(64) NOT NULL, -- ユーザーID
   `status` VARCHAR(64) NOT NULL, -- タスクのステータス
   title VARCHAR(64) NOT NULL, -- タスクのタイトル
   dueDate DATE NOT NULL, -- タスクの締め切り
   memo VARCHAR(64), -- タスクのコメント
   createdOn TIMESTAMP NOT NULL, -- タスクを作成した日時
   updatedOn TIMESTAMP NOT NULL -- タスクを更新した日時
);
