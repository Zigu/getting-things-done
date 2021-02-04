export function saveProject(state, project) {
  const foundProjects = state.projects.filter((p) => p.id === project.id);
  if (foundProjects.length === 1) {
    const index = state.projects.indexOf(foundProjects[0]);
    state.projects[index] = project;
  } else {
    state.projects.push(project);
  }
}

export function replaceState(state, projects) {
  const newState = projects
    .map((project) => ({ id: project.id, version: project.version, name: project.name }));
  state.projects.splice(0, state.projects.length, ...newState);
}
