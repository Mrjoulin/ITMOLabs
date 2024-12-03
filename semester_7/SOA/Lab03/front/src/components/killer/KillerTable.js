import React, { useState, useEffect } from 'react';
import { Box, Container, Grid, TextField, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import { createTeam, moveTeam } from '../../services/killerService'


const KillerTable = ({ makeApiRequest }) => {
  const [teamName, setTeamName] = useState('');
  const [teamSize, setTeamSize] = useState('');
  const [startCaveId, setStartCaveId] = useState('');
  const [teams, setTeams] = useState([]);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedTeam, setSelectedTeam] = useState(null);
  const [newCaveId, setNewCaveId] = useState('');
  const [validateError, setValidateError] = useState({
    teamName: '',
    teamSize: '',
    startCaveId: '',
    newCaveId: ''
  })

  
  const validateFiled = (field, value) => {
    if (field == 'teamSize' || field == 'startCaveId' || field == 'newCaveId') {
      if (isNaN(value) || !(/^\+?([1-9]\d*)$/.test(value))) {
        setValidateError({ ...validateError, [field]: "Invalid integer" });
        return false
      } else {
        setValidateError({ ...validateError, [field]: "" });
        return true
      }
    } else {
      if (value.length > 64) {
        setValidateError({ ...validateError, [field]: "Too long" });
        return false
      } else if (value.trim() == '') {
        setValidateError({ ...validateError, [field]: "Must be not empty" });
        return false
      } else {
        setValidateError({ ...validateError, [field]: "" });
        return true
      }
    }
  }

  const handleTeamNameChange = (value) => {
    validateFiled("teamName", value);
    setTeamName(value);
  };
  const handleTeamSizeChange = (value) => {
    validateFiled("teamSize", value);
    setTeamSize(value);
  };
  const handleStartCaveIdChange = (value) => {
    validateFiled("startCaveId", value);
    setStartCaveId(value);
  };
  const handleNewCaveIdChange = (value) => {
    validateFiled("newCaveId", value);
    setNewCaveId(value);
  };

  const handleCreateTeam = async () => {
    var isValid = validateFiled("teamName", teamName) && validateFiled("teamSize", teamSize) && validateFiled("startCaveId", startCaveId)
    if (!isValid) return

    try {
      const response = await makeApiRequest(createTeam, teamName, teamSize, startCaveId)
      if (response != null) {
        const data = response.data;
    
        setTeams([...teams, data]);
        setTeamName('');
        setTeamSize('');
        setStartCaveId('');
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleMoveTeam = async () => {
    if (!validateFiled("newCaveId", newCaveId))
      return

    try {
      const response = await makeApiRequest(moveTeam, selectedTeam.id, newCaveId)
      const data = response.data;
      const updatedTeams = teams.map((team) => (team.id === selectedTeam.id ? data : team));
      setTeams(updatedTeams);
      setOpenDialog(false);
      setNewCaveId('');
    } catch (error) {
      console.error(error);
    }
  };

  const handleEditTeam = (team) => {
    setSelectedTeam(team);
    setOpenDialog(true);
  };

  return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'center' }}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <h2>Killer and teams</h2>
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField 
              required
              label="Team Name" 
              value={teamName} 
              onChange={(e) => handleTeamNameChange(e.target.value)} 
              error={validateError.teamName != '' ? true : false}
              helperText={validateError.teamName}
              fullWidth 
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField 
              required
              label="Team Size" 
              value={teamSize} 
              onChange={(e) => handleTeamSizeChange(e.target.value)} 
              error={validateError.teamSize != '' ? true : false}
              helperText={validateError.teamSize}
              fullWidth 
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField 
              required
              label="Start Cave Id" 
              value={startCaveId} 
              onChange={(e) => handleStartCaveIdChange(e.target.value)} 
              error={validateError.startCaveId != '' ? true : false}
              helperText={validateError.startCaveId}
              fullWidth 
            />
          </Grid>
          <Grid item xs={12}>
            <Button variant="contained" onClick={handleCreateTeam}>
              Create Team
            </Button>
          </Grid>
          <Grid item xs={12}>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Id</TableCell>
                    <TableCell>Creation Date</TableCell>
                    <TableCell>Name</TableCell>
                    <TableCell>Size</TableCell>
                    <TableCell>Cave Id</TableCell>
                    <TableCell>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {teams.map((team) => (
                    <TableRow key={team.id}>
                      <TableCell>{team.id}</TableCell>
                      <TableCell>{team.creationDate}</TableCell>
                      <TableCell>{team.name}</TableCell>
                      <TableCell>{team.size}</TableCell>
                      <TableCell>{team.caveId}</TableCell>
                      <TableCell>
                        <IconButton onClick={() => handleEditTeam(team)}>
                          <EditIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Grid>
        </Grid>
      </Box>
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>Move Team</DialogTitle>
        <DialogContent>
          <DialogContentText>Enter new cave id:</DialogContentText>
          <TextField 
            required
            label="New Cave Id" 
            value={newCaveId} 
            onChange={(e) => handleNewCaveIdChange(e.target.value)}
            error={validateError.newCaveId != '' ? true : false}
            helperText={validateError.newCaveId}
            fullWidth 
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
          <Button onClick={handleMoveTeam}>Move</Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default KillerTable;