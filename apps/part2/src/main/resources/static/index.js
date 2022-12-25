"use strict";
(function () {
    const $ = document;

    // 今日の日付で締め切りの初期値を設定する
    $.querySelector("#new-todo-duedate").valueAsDate = new Date();

    // 初期描画や更新ボタン押下時にToDoリストを描画する処理を呼ぶ
    window.onload = renderTodoList;
    $.querySelector("#refresh-todo").addEventListener("click", renderTodoList);

    // 送信ボタンを押したら、タスクを作成する
    $.querySelector("#new-todo-submit").addEventListener("click", doPost);

    // HTTP APIでタスクを作成し、ToDoリストを再描画する
    function doPost () {
        const title = $.querySelector("#new-todo-title").value;
        const dueDate = $.querySelector("#new-todo-duedate").value;
        const memo = $.querySelector("#new-todo-memo").value;

        if (title == '') {
            return;
        }

        fetch("/api/todo", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "title": title,
                "dueDate": dueDate,
                "memo": memo
            })
        }).then(res => {
            $.querySelector("#new-todo-title").value = "";
            $.querySelector("#new-todo-duedate").valueAsDate = new Date();
            $.querySelector("#new-todo-memo").value = "";

            return res.json();
        }).then(renderTodoList)
        .catch(error => console.error(error));
    }

    // HTTP APIから取得したタスクでToDoリストを描画する
    function renderTodoList() {
        fetch("/api/todo", function () {
            method: "GET"
        }).then(res => {
            return res.json();
        }).then(todos => {
            const todolist = $.querySelector("#todolist-table tbody");

            // <tbody> の中身を空にする
            while (todolist.firstChild) {
                todolist.removeChild(todolist.firstChild);
            }

            // ToDo が 0 個の場合は後続の処理は不要
            if (todos.length == 0) {
                return;
            }

            for (let i=0; i<todos.length; ++i) {
                let newRow = `
                <tr class="todo-task">
                  <td class="task-id">${todos[i].id}</td>
                  <td class="task-title">${todos[i].title}</td>
                  <td class="task-memo">${todos[i].memo}</td>
                  <td class="task-status">
                    <select class="form-select">
                      <option value="Created" ${todos[i].status == "Created" ? "selected" : ""}>新規</option>
                      <option value="Started" ${todos[i].status == "Started" ? "selected" : ""}>着手済</option>
                      <option value="Done" ${todos[i].status == "Done" ? "selected" : ""}>完了</option>
                    </select>
                  </td>
                  <td class="task-duedate">
                    <input type="date" class="form-control" value="${todos[i].dueDate}" />
                  </td>
                  <td><button type="button" class="btn btn-danger delete-task">削除</button></td>
                </tr>
                `;

                todolist.insertAdjacentHTML("beforeend", newRow);
            }

            // 値の変化でタスクの変更処理が起きるようにする
            $.querySelectorAll(".todo-task").forEach(elem => {
                elem.addEventListener("change", doUpdate);
            });

            // 削除ボタンクリック時にタスクを削除する
            $.querySelectorAll(".delete-task").forEach(elem => {
                elem.addEventListener("click", doDelete);
            });

        }).catch(error => console.error(error));
    }

    // HTTP APIでタスクを更新し、ToDoリストを再描画する
    function doUpdate() {
        const currentRow = this;

        fetch("/api/todo", {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "id": currentRow.querySelector(".task-id").textContent,
                "status": currentRow.querySelector(".task-status > select").value,
                "title": currentRow.querySelector(".task-title").textContent,
                "dueDate": currentRow.querySelector(".task-duedate > input").value,
                "memo": currentRow.querySelector(".task-memo").textContent
            })
        }).finally (() => {
            renderTodoList();
        });
    }

    // HTTP APIでタスクを削除し、ToDoリストを再描画する
    function doDelete () {
        const currentRow = this.closest(".todo-task");

        fetch("/api/todo", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "id": currentRow.querySelector(".task-id").textContent
            })
        }).finally (() => {
            renderTodoList();
        });
    }

    //------------------- 第6章で使用するサインアウト -------------------
    const signOutLink = "/.auth/logout?post_logout_redirect_uri=" + encodeURI("/?a=") + new Date().getTime(); 
    let isSignedIn = false;
    let userId = "?";

    window.addEventListener("load", showUserName());
    $.querySelector("#sign-out-btn").addEventListener("click", signOut);

    // 認証したユーザーのメールアドレスを表示
    function showUserName() {
        fetch("/.auth/me", function () {
            method: "GET"
        }).then(res => {
            if (!res.ok) {
                throw new Error("Network response was not OK");
            }
            return res.json();
        }).then(json => {
            if(!Object.keys(json).length){
                throw new Error("Invalid response");
            }
            isSignedIn = true;
            userId = json[0].user_id;
        }).catch(error => {
            isSignedIn = false;
            console.error("Error: ", error);
        }).finally(() => {
            if (isSignedIn) {
                $.querySelector("#sign-out-btn").innerHTML = "Sign out of " + userId;
            }
        })
    }
    
    // サインアウト
    function signOut() {
        $.cookie = "AppServiceAuthSession=; max-age=0";
        isSignedIn = false;
        location.href = signOutLink;
    }

})();