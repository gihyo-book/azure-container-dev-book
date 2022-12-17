import { useContext } from 'react';
import TodoContext from '../../context/TodoContext';
import '../../Bootstrap.css';


const Todos = () => {
  const { todos, isLoading, deleteTodo, editTodo } = useContext(TodoContext);

  const handleDelete = (id) => {
    deleteTodo(id);
  }


  if (!isLoading && !todos && todos.length === 0) {
    return <h5>データがありません</h5>;
  }
  return isLoading ? (
    <h5>Loading ...</h5>
  ) : (
    <div className='container'>
      <table className='table table-striped'>
        <thead>
          <tr>
            <th scope='col'>#</th>
            <th scope='col'>タイトル</th>
            <th scope='col'>タスク</th>
            <th scope='col'>ステータス</th>
            <th scope='col'>締め切り</th>
            <th scope='col'></th>
          </tr>
        </thead>
        <tbody>
          {todos.map((todo, index) => (
            <tr key={todo.id}>
              <th scope='row'>{index + 1}</th>
              <td>{todo.title}</td>
              <td>{todo.task}</td>
              <td>
                <span>
                  {todo.status === 'Completed' ? 'Completed' : 'Pending'}
                </span>
              </td>
              <td>{todo.duedate}</td>
              <td>
                <button type='button' className='btn btn-primary' onClick={() => editTodo(todo)}>
                  更新
                </button>
                <button type='button' className='btn btn-dark' onClick={() => handleDelete(todo.id)}>
                  削除
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Todos;