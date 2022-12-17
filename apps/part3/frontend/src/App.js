import { TodoProvider } from "./context/TodoContext";
import { ScheduleProvider } from "./context/ScheduleContext";
import Todo from "./components/todo/Todo";
import Todos from "./components/todos/Todos";
import Schedule from "./components/schedule/Schedule";
import "./App.css";
import { ApplicationInsights } from '@microsoft/applicationinsights-web'

const BACKEND_API_URL = process.env.REACT_APP_BACKEND_API_URL;
const APPINSIGHTS_CONNECTION_STRING = process.env.REACT_APP_APPINSIGHTS_CONNECTION_STRING;

const appInsights = new ApplicationInsights({
  config: {
    connectionString: APPINSIGHTS_CONNECTION_STRING
    /* ...Other Configuration Options... */
  }
});
appInsights.loadAppInsights();
appInsights.trackPageView();


function App() {

  return (
    <div>
      <p class="text-muted">{BACKEND_API_URL}</p>
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark mb-5">
        <div className="container-fluid">
          <a className="navbar-brand" href="/">Todoアプリケーション</a>
        </div>
        <div class="d-flex">
          <a href="/.auth/login/aad" class="btn btn-secondary" role="button">Login</a>
          <a href="/.auth/logout/aad" class="btn btn-secondary" role="button">Logout</a>
        </div>
      </nav>
      <div className="container">
      <ScheduleProvider>
          <Schedule />
      </ScheduleProvider>
      <TodoProvider>
          <Todo />
          <hr />
          <Todos />
      </TodoProvider>
      </div>
    </div>
  );
}

export default App;