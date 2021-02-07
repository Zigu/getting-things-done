export function saveTopic(state, topic) {
  const foundTopics = state.topics.filter((p) => p.id === topic.id);
  if (foundTopics.length === 1) {
    const index = state.topics.indexOf(foundTopics[0]);
    state.topics[index] = topic;
  } else {
    state.topics.push(topic);
  }
}

export function replaceState(state, topics) {
  const newState = topics
    .map((topic) => ({ id: topic.id, version: topic.version, name: topic.name }));
  state.topics.splice(0, state.topics.length, ...newState);
}
