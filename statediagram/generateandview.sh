#GRAPH_CMD=dot
GRAPH_CMD=neato
INFILE=replica_states.dot

$GRAPH_CMD -Tps $INFILE | gv -
