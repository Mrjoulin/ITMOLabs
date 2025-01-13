import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { TextField, Button, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';

const DragonService = () => {
  const [dragons, setDragons] = useState([]);
  const [sort, setSort] = useState('name');
  const [filters, setFilters] = useState('');
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchDragons();
  }, []);

  const fetchDragons = async () => {
      const response = await axios.post('https://localhost:1234/api/v1/dragons/array', {
        sort: [sort],
        filter: filters ? [filters.split(' ')] : [],
        page: 0,
        pageSize: 10
      })
      .then(function(response){
        setDragons(response.data.results);
      })
      .catch (function(err) {
        setError(`Ошибка: ${err.message || 'Неизвестная ошибка'}`);
      });
  };

  return (
    <div>
      <h2>Драконы</h2>
      <TextField label="Сортировать по" value={sort} onChange={(e) => setSort(e.target.value)} />
      <TextField label="Фильтры" value={filters} onChange={(e) => setFilters(e.target.value)} />
      <Button onClick={fetchDragons}>Поиск драконов</Button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>ID</TableCell>
            <TableCell>Имя</TableCell>
            <TableCell>Возраст</TableCell>
            <TableCell>Цвет</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {dragons.map((dragon) => (
            <TableRow key={dragon.id}>
              <TableCell>{dragon.id}</TableCell>
              <TableCell>{dragon.name}</TableCell>
              <TableCell>{dragon.age}</TableCell>
              <TableCell>{dragon.color}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
};

export default DragonService;