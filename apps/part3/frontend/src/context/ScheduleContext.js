import { createContext, useState, useEffect } from "react";
import Schedule from "../api/Backend";

const ScheduleContext = createContext();

export const ScheduleProvider = ({ children }) => {
  const [schedule, setSchedule] = useState([]);
  const [isError, setIsError] = useState(false);

  useEffect(() => {
    fetchSchedule();
  }, []);

  // GET method
  const fetchSchedule= async () => {
    try {
      const response = await Schedule.get(
        "/schedule"
      );
      setSchedule(response.data)    
    } catch (error) {
      setIsError(true)
    }
  };

  return (
    <ScheduleContext.Provider
      value={{
        schedule,
        isError
      }}
    >
      {children}
    </ScheduleContext.Provider>
  );
};

export default ScheduleContext;