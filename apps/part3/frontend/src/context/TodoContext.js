import { createContext, useState, useEffect } from "react";
import Backend from "../api/Backend";

const TodoContext = createContext();

export const TodoProvider = ({ children }) => {
  const [todos, setTodos] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [edit, setTodoEdit] = useState({ todo: {}, isEdit: false });


  useEffect(() => {
    fetchTodos();
  }, []);


  // GET method
  const fetchTodos = async () => {
    const response = await Backend.get('/todos');
    setTodos(response.data);
    setIsLoading(false);
  };

  // POST method
  const addTodo = async (newTodo) => {
    try {
      const response = await Backend.post('/todos', newTodo);
      const data = response.data;
      setTodos([data, ...todos]);
    } catch (error) {
    }
  };

  // PUT method
  const updateTodo = async (id, todo) => {
    try {
      const response = await Backend.put(`/todos/${id}`, todo);
      const data = response.data;
      setTodos(
        todos.map((todo) => (todo.id === id ? { ...todo, ...data } : todo))
      );
    } catch (error) {
    }
  };

  // DELETE method
  const deleteTodo = async (id) => {
    try {
      await Backend.delete(`/todos/${id}`);
      setTodos(todos.filter((todo) => todo.id !== id));
    } catch (error) {
    }
  };

  const editTodo = (todo) => {
    setTodoEdit({ todo, isEdit: true });
  };

  return (
    <TodoContext.Provider
      value={{
        todos,
        isLoading,
        edit,
        addTodo,
        updateTodo,
        deleteTodo,
        editTodo
      }}
    >
      {children}
    </TodoContext.Provider>
  );
};

export default TodoContext;