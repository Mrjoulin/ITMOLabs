import React, { useState } from 'react';
import axios from 'axios';
import { TextField, Button } from '@mui/material';

function KillerService () {
  const [teamName, setTeamName] = useState('');
  const [teamSize, setTeamSize] = useState('');
  const [startCaveId, setStartCaveId] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const createTeam = async () => {
      const response = await axios.post(`https://localhost:3000/api/v1/killer/teams/create/${teamName}/${teamSize}/${startCaveId}`)
      .then(function (response) {
        setResult(`Команда создана, ID команды: ${response.data}`);
      })
      .catch(function(err){
        setError(`Ошибка: ${err.message || 'Неизвестная ошибка'}`);
      });
  };

  return (
    <div class='root2'>
      <h2>Создать команду</h2>
      <TextField label="Название команды" value={teamName} onChange={(e) => setTeamName(e.target.value)} />
      <TextField label="Размер команды" type="number" value={teamSize} onChange={(e) => setTeamSize(e.target.value)} />
      <TextField label="ID пещеры" type="number" value={startCaveId} onChange={(e) => setStartCaveId(e.target.value)} />
      <Button onClick={createTeam}>Создать команду</Button>
      {result && <p>{result}</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
}

export default KillerService;