import axios from 'axios';

const API_BASE = 'https://localhost:7500/api/v1/caves';

export const getCaves = () => {
  return axios.get(`${API_BASE}/`);
};

export const createCave = (cave) => {
  return axios.post(`${API_BASE}/`, cave);
};

export const updateCave = (caveId, cave) => {
  return axios.put(`${API_BASE}/${caveId}`, cave);
};

export const deleteCave = (caveId) => {
  return axios.delete(`${API_BASE}/${caveId}`);
};