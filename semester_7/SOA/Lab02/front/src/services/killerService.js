import axios from 'axios';

const API_BASE = 'https://localhost:9000/killer-1.0/api/v1/killers';

export const createTeam = (teamName, teamSize, startCaveId) => {
  return axios.post(`${API_BASE}/teams/create/${teamName}/${teamSize}/${startCaveId}`);
};

export const moveTeam = (teamId, caveId) => {
  return axios.put(`${API_BASE}/team/${teamId}/move/${caveId}`);
};