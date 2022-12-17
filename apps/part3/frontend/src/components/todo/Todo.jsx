import { useContext, useEffect, useState } from 'react';
import TodoContext from '../../context/TodoContext';
import '../../Bootstrap.css';

const Todo = () => {
  const [task, setTask] = useState('');
  const [status, setStatus] = useState('');
  const [title, setTitle] = useState('');
  const [duedate, setDuedate] = useState('');
  const { edit, addTodo, updateTodo } = useContext(TodoContext);

  useEffect(() => {
    if (edit.isEdit) { // check edit or new todo mode
      setTask(edit.todo.task);
      setTitle(edit.todo.title);
      setStatus(edit.todo.status);
      setDuedate(edit.todo.duedate);
    }
  }, [edit]);

  const submit = (e) => {
    e.preventDefault();
    const todo = { task, title, status, duedate };
    if (edit.isEdit) {
      updateTodo(edit.todo.id, todo);
      clear();
    } else {
      addTodo(todo);
      clear();
    }
  };

  const clear = () =>{
    setTitle("");
    setTask("");
    setStatus("");
    setDuedate("");
    edit.isEdit = false;
  }

  return (
    <div className='form-container'>
      <form onSubmit={submit}>
        <div className='form-group'>
          <label>新しいタスク</label>
          <input
            className='form-control'
            id='taskTitle'
            placeholder='Title'
            onChange={e => setTitle(e.target.value)}
            name='title'
            value={title}
          />
        </div>
        <div className='form-group'>
          <label>タスクの内容は？</label>
          <input
            className='form-control'
            id='taskId'
            placeholder='Task'
            onChange={e => setTask(e.target.value)}
            name='task'
            value={task}
          />
        </div>
        <div className='form-group'>
          <label>締め切り</label>
          <input
            className='form-control'
            id='taskDuedate'
            type="date"
            onChange={e => setDuedate(e.target.value)}
            name='duedate'
            value={duedate}
          />
        </div>    
        <div className='form-group mt-3 mb-4'>
          <div className="btn-group" role="group">
            <input
              className='btn-check'
              type='radio'
              id="pending"
              name='status'
              value='Pending'
              checked={status === 'Pending' ? true : false}
              onChange={e => setStatus(e.target.value)}
            />
            <label className="btn btn-outline-primary" htmlFor="pending">Pending</label>
            <input
              className='btn-check'
              type='radio'
              id="completed"
              name='status'
              value='Completed'
              checked={status === 'Completed' ? true : false}
              onChange={e => setStatus(e.target.value)}
            />
            <label className="btn btn-outline-primary" htmlFor="completed">Completed</label>
          </div>
        </div>
        <button
          type='submit'
          className='btn btn-primary'>
          登録
        </button>
      </form>
    </div>
  );
};

export default Todo;