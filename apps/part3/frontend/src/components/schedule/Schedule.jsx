import { useContext } from 'react';
import ScheduleContext from '../../context/ScheduleContext';
import '../../Bootstrap.css';

const Schedule = () => {
  const { schedule } = useContext(ScheduleContext);
  const { isError } = useContext(ScheduleContext);

  if (isError) {
    return (
      <div className="alert alert-dismissible alert-danger">
        <strong>スケジュールデータが取得できません</strong>
      </div>
    );
  } else {
    return (
      <div className="alert alert-dismissible alert-light">
        <strong>今日のスケジュール</strong>
        <ul>
          {schedule.map((schedule) => (
            <li key={schedule.id}><b>{schedule.time}</b>   {schedule.title}</li>
          ))}
        </ul>
      </div>

    );
  }
};

export default Schedule;