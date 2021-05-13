# rsk-nbcm
## RSK Node Bandwidth Consumption Monitor

This tool  instruments [RSKj](https://github.com/rsksmart/rskj) to collect and export bandwidth consumption data.

The bandwidth consumption is categorised by wire message type. The block synchronisation distinguishes between blockchain tip synchronization, and old data synchronization.

The application measures in-bound and out-bound bandwidth separately.

## Execution

The tool collects data using instrumentation. No changes to the RSKj are necessary. The tool gets executes as a java agent. The RSKj startup script must contain a javaagent parameter such as

```-javaagent:rsk-nbcm-0.1.0.jar  ```

See [sample startup script](doc/rsk-nbcm)


## Data file structure

Output data are logged into a CSV file named `nbcm-trace.csv` in the current directory. The file contains the following collumns:

1. Block receive time
2. Character I if the message is Inbound or character O if the message is outbound.
3. Message size
4. Message command (code) or BEST_BLOCK_CHANGE in the case of extending the best chain by one block
5. When sending a block, receiving a block or extending the best chain, log the block height
 

Example:

```
"2021-05-12T17:03:39.611411Z","O","STATUS",82,-1
"2021-05-12T17:03:39.618605Z","O","BLOCK_HEADERS_REQUEST_MESSAGE",41,-1
"2021-05-12T17:03:39.621401Z","O","STATUS_MESSAGE",89,-1
"2021-05-12T17:03:39.622621Z","O","STATUS_MESSAGE",89,-1
"2021-05-12T17:03:39.838026Z","I","BLOCK_HEADERS_RESPONSE_MESSAGE",942,-1
"2021-05-12T17:03:39.868454Z","O","BLOCK_HASH_REQUEST_MESSAGE",11,-1
"2021-05-12T17:03:40.126899Z","I","BLOCK_HASH_RESPONSE_MESSAGE",40,-1
"2021-05-12T17:03:40.139755Z","O","BLOCK_HASH_REQUEST_MESSAGE",11,-1
"2021-05-12T17:03:40.382898Z","I","BLOCK_HASH_RESPONSE_MESSAGE",40,-1
```


## Usage example 

The file can be analysed by any data analysis tool. The `doc` directory contains an example `pandas` script to load the file into pandas environment to analyse the data and plot results.
[Example](doc/pandas.pdf)


## Note


RSK Node Bandwidth monitor has been created during the RSK Hackathon: Building theFuture on Bitcoin.
