## Network protocol

| Instruction | p1 | p2 | p3 | p4 | Effect                                           |
|-------------|----|----|----|----|--------------------------------------------------|
| GET         | x1 | y1 | x2 | y2 | get all nodes from (x1 y1) to (x2 y2), inclusive |
| PUT         | x  | y  | n  |    | put node n at (x y)                              |
| REMOVE      | x  | y  | n  |    | remove node n from (x y)                         |

### How it works:
1. Client requests chunks from server using the GET instruction,
   until local cache is filled enough
2. When user adds/removes nodes, client checks whether action is
   valid, and if so, does it on the local copy of the world and
   sends request to the server to do that action
3. When the world on the server changes, users who have cached
   appropriate nodes are notified