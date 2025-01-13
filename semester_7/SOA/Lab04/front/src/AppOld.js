import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import DragonService from './DragonService';
import KillerService from './KillerService';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/dragons" element={<DragonService />} />
        <Route path="/killer" element={<KillerService />} />
      </Routes>
    </Router>
  );
}

export default App;
