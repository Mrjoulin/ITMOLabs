import React, { useEffect, useState } from 'react';
import { getCaves, deleteCave } from '../../services/caveService';
import { Table, TableBody, TableCell, TableHead, TableRow, IconButton } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';

const CaveTable = ({ onEdit, makeApiRequest, fetchCavesFlag }) => {
  const [caves, setCaves] = useState([]);

  useEffect(() => {
    fetchCaves();
  }, [fetchCavesFlag]);

  const fetchCaves = async () => {
    const response = await makeApiRequest(getCaves);
    if (response != null){
      setCaves(response.data);
    }
    fetchCavesFlag = false
  };

  const handleDelete = async (caveId) => {
    await makeApiRequest(deleteCave, caveId);
    fetchCaves();
  };

  const handleEdit = (cave) => {
    onEdit(cave);  // Pass cave data to edit form
  };

  return (
    <div>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>ID</TableCell>
            <TableCell>Coordinates (X, Y)</TableCell>
            <TableCell>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {caves.map((cave) => (
            <TableRow key={cave.id}>
              <TableCell>{cave.id}</TableCell>
              <TableCell>{`(${cave.coordinates.x}, ${cave.coordinates.y})`}</TableCell>
              <TableCell>
                <IconButton onClick={() => handleEdit(cave)}>
                  <EditIcon />
                </IconButton>
                <IconButton onClick={() => handleDelete(cave.id)}>
                  <DeleteIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
};

export default CaveTable;