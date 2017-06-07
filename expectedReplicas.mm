<map version="freeplane 1.2.0">
<!--To view this file, download free mind mapping software Freeplane from http://freeplane.sourceforge.net -->
<node TEXT="root" ID="ID_1723255651" CREATED="1283093380553" MODIFIED="1496820922146"><hook NAME="MapStyle">

<map_styles>
<stylenode LOCALIZED_TEXT="styles.root_node">
<stylenode LOCALIZED_TEXT="styles.predefined" POSITION="right">
<stylenode LOCALIZED_TEXT="default" MAX_WIDTH="600" COLOR="#000000" STYLE="as_parent">
<font NAME="SansSerif" SIZE="10" BOLD="false" ITALIC="false"/>
</stylenode>
<stylenode LOCALIZED_TEXT="defaultstyle.details"/>
<stylenode LOCALIZED_TEXT="defaultstyle.note"/>
<stylenode LOCALIZED_TEXT="defaultstyle.floating">
<edge STYLE="hide_edge"/>
<cloud COLOR="#f0f0f0" SHAPE="ROUND_RECT"/>
</stylenode>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.user-defined" POSITION="right">
<stylenode LOCALIZED_TEXT="styles.topic" COLOR="#18898b" STYLE="fork">
<font NAME="Liberation Sans" SIZE="10" BOLD="true"/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.subtopic" COLOR="#cc3300" STYLE="fork">
<font NAME="Liberation Sans" SIZE="10" BOLD="true"/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.subsubtopic" COLOR="#669900">
<font NAME="Liberation Sans" SIZE="10" BOLD="true"/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.important">
<icon BUILTIN="yes"/>
</stylenode>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.AutomaticLayout" POSITION="right">
<stylenode LOCALIZED_TEXT="AutomaticLayout.level.root" COLOR="#000000">
<font SIZE="18"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,1" COLOR="#0033ff">
<font SIZE="16"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,2" COLOR="#00b439">
<font SIZE="14"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,3" COLOR="#990000">
<font SIZE="12"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,4" COLOR="#111111">
<font SIZE="10"/>
</stylenode>
</stylenode>
</stylenode>
</map_styles>
</hook>
<hook NAME="AutomaticEdgeColor" COUNTER="8"/>
<node TEXT="addExpectedReplica" POSITION="left" ID="ID_1039148266" CREATED="1496820898317" MODIFIED="1496820930812">
<edge COLOR="#00ffff"/>
<node TEXT="setExpectedLocations" ID="ID_1124580691" CREATED="1496817173148" MODIFIED="1496820907399">
<node TEXT="convertToBlockUnderConstruction" ID="ID_1229759813" CREATED="1496817475729" MODIFIED="1496819914605">
<font BOLD="false"/>
<node TEXT="setLastBlock" ID="ID_596834858" CREATED="1496818225779" MODIFIED="1496818228352">
<node TEXT="commitBlockSynchronization" ID="ID_1086073246" CREATED="1496818254555" MODIFIED="1496818268383">
<node TEXT="dn.commitBlockSynchronization" ID="ID_798335840" CREATED="1496818269147" MODIFIED="1496818282306">
<font BOLD="true"/>
</node>
</node>
<node TEXT="convertLastBlockToUnderConstruction" ID="ID_1434885616" CREATED="1496818307050" MODIFIED="1496818318679">
<node TEXT="prepareFileForWrite" ID="ID_40701374" CREATED="1496818319138" MODIFIED="1496818322791">
<node TEXT="startFileInternal" ID="ID_778808751" CREATED="1496818331210" MODIFIED="1496818361810">
<font BOLD="true"/>
</node>
</node>
</node>
</node>
</node>
<node TEXT="BlockInfoUnderConstruction" ID="ID_532916258" CREATED="1496817491449" MODIFIED="1496817499078">
<node TEXT="convertToBlockUnderConstruction" ID="ID_1361053186" CREATED="1496818088628" MODIFIED="1496818369938">
<font BOLD="true"/>
</node>
<node TEXT="FSDirectory.addBlock" ID="ID_410240150" CREATED="1496818102847" MODIFIED="1496819923773">
<font BOLD="false"/>
<node TEXT="saveAllocatedBlock" ID="ID_969914706" CREATED="1496818134020" MODIFIED="1496818146200">
<node TEXT="getAdditionalBlock" ID="ID_1776974404" CREATED="1496818146628" MODIFIED="1496818150640">
<node TEXT="client.addBlock" ID="ID_639069400" CREATED="1496818156764" MODIFIED="1496818164627">
<font BOLD="true"/>
</node>
</node>
</node>
</node>
</node>
<node TEXT="internalReleaseLease" ID="ID_1100962964" CREATED="1496817510393" MODIFIED="1496817514542">
<node TEXT="checkLeases" ID="ID_1783300385" CREATED="1496817645624" MODIFIED="1496817649429">
<node TEXT="LeaseManager.Monitor.run" ID="ID_668120338" CREATED="1496818014653" MODIFIED="1496818026549">
<font BOLD="true"/>
</node>
</node>
<node TEXT="recoverLeaseInternal (x2)" ID="ID_148196628" CREATED="1496817656736" MODIFIED="1496817689740">
<node TEXT="startFileInternal" ID="ID_1416281870" CREATED="1496817708175" MODIFIED="1496819909871">
<font BOLD="false"/>
<node TEXT="appendFileInt" ID="ID_1112512173" CREATED="1496817768607" MODIFIED="1496817771403">
<node TEXT="appendFile" ID="ID_893702321" CREATED="1496817781287" MODIFIED="1496817783107">
<node TEXT="client.appendFile" ID="ID_224260362" CREATED="1496817788359" MODIFIED="1496818163691">
<font BOLD="true"/>
</node>
</node>
</node>
<node TEXT="startFileInt" ID="ID_1289535838" CREATED="1496817827704" MODIFIED="1496817830811">
<node TEXT="startFile" ID="ID_818541736" CREATED="1496817837350" MODIFIED="1496817846822">
<node TEXT="client.create" ID="ID_785763969" CREATED="1496817846825" MODIFIED="1496817856854">
<font BOLD="true"/>
</node>
</node>
</node>
</node>
<node TEXT="recoverLease" ID="ID_828732548" CREATED="1496817720943" MODIFIED="1496817731150">
<node TEXT="client.recoverLease" ID="ID_473712109" CREATED="1496817732815" MODIFIED="1496817739255">
<font BOLD="true"/>
</node>
</node>
</node>
</node>
<node TEXT="updatePipelineInternal" ID="ID_1451224848" CREATED="1496817521665" MODIFIED="1496817526166">
<node TEXT="updatePipeline" ID="ID_392214470" CREATED="1496817539321" MODIFIED="1496817554737">
<node TEXT="client.updatePipeline" ID="ID_1139874007" CREATED="1496817554740" MODIFIED="1496817613600">
<font BOLD="true"/>
</node>
</node>
</node>
</node>
<node TEXT="addReplicaIfNotPresent" ID="ID_288703724" CREATED="1496817186596" MODIFIED="1496820917407">
<node TEXT="addStoredBlockUnderConstruction" ID="ID_733926113" CREATED="1496817275227" MODIFIED="1496817291779">
<node TEXT="processAndHandleReportedBlock" ID="ID_1256409353" CREATED="1496817291781" MODIFIED="1496819998844">
<font BOLD="false"/>
<node TEXT="addBlock" ID="ID_160638675" CREATED="1496819947565" MODIFIED="1496819949954">
<node TEXT="processIncrementalBlockReport" ID="ID_531057022" CREATED="1496819974309" MODIFIED="1496819995764">
<font BOLD="true"/>
</node>
</node>
<node TEXT="processIncrementalBlockReport" ID="ID_172938924" CREATED="1496819950533" MODIFIED="1496819972641">
<node TEXT="dn.blockReceivedDeleted" ID="ID_582727805" CREATED="1496819988053" MODIFIED="1496819994501">
<font BOLD="true"/>
</node>
</node>
</node>
<node TEXT="addStoredBlockUnderConstructionTx" ID="ID_189391014" CREATED="1496817309775" MODIFIED="1496817323587">
<node TEXT="processFullReport" ID="ID_1847781298" CREATED="1496817323591" MODIFIED="1496820044012">
<font BOLD="false"/>
<node TEXT="processReport" ID="ID_1977929007" CREATED="1496820014476" MODIFIED="1496820016720">
<node TEXT="dn.blockReport" ID="ID_1088687757" CREATED="1496820030436" MODIFIED="1496820043228">
<font BOLD="true"/>
</node>
</node>
</node>
</node>
</node>
</node>
</node>
<node TEXT="updatePipeline" POSITION="right" ID="ID_296287143" CREATED="1496820940469" MODIFIED="1496820944441">
<edge COLOR="#7c0000"/>
<node TEXT="DFSClient.updatePipeline" ID="ID_1981897851" CREATED="1496820619543" MODIFIED="1496820955646">
<node TEXT="setupPipelineForAppendOrRecovery" ID="ID_226878560" CREATED="1496820639591" MODIFIED="1496820648019">
<node TEXT="DataStreamer.run" ID="ID_1789259510" CREATED="1496820660895" MODIFIED="1496820821709">
<font BOLD="true"/>
</node>
<node TEXT="processDatanodeError" ID="ID_468542503" CREATED="1496820683464" MODIFIED="1496820702596">
<node TEXT="DataStreamer.run" ID="ID_761371544" CREATED="1496820703943" MODIFIED="1496820715579">
<node TEXT="newStreamForCreate" ID="ID_479901784" CREATED="1496820782910" MODIFIED="1496820822334">
<font BOLD="true"/>
</node>
</node>
</node>
</node>
</node>
</node>
<node TEXT="&quot;deleteExpectedReplica&quot;" POSITION="left" ID="ID_1879491075" CREATED="1496820976837" MODIFIED="1496820986058">
<edge COLOR="#00007c"/>
<node TEXT="complete" ID="ID_1403215987" CREATED="1496818740151" MODIFIED="1496820991096">
<node TEXT="convertToCompleteBlock" ID="ID_236823094" CREATED="1496818749495" MODIFIED="1496818752787">
<node TEXT="completeBlock" ID="ID_878101264" CREATED="1496818760535" MODIFIED="1496818762667">
<node TEXT="commitOrCompleteLastBlock" ID="ID_1475628739" CREATED="1496818948621" MODIFIED="1496818953745">
<node TEXT="FSNamesystem.commitOrCompleteLastBlock" ID="ID_1649623882" CREATED="1496819443753" MODIFIED="1496819455517">
<node TEXT="getAdditionalBlock" ID="ID_14595206" CREATED="1496819460857" MODIFIED="1496819470557">
<node TEXT="client.addBlock" ID="ID_1635530938" CREATED="1496819625112" MODIFIED="1496819629647">
<font BOLD="true"/>
</node>
</node>
<node TEXT="completeFileInternal" ID="ID_1467698882" CREATED="1496819472537" MODIFIED="1496819486725">
<node TEXT="completeFile" ID="ID_198405411" CREATED="1496819560112" MODIFIED="1496819563261">
<node TEXT="client.complete" ID="ID_1339201722" CREATED="1496819576256" MODIFIED="1496819606320">
<font BOLD="true"/>
</node>
</node>
</node>
<node TEXT="commitBlockSynchronization" ID="ID_1904670664" CREATED="1496819530314" MODIFIED="1496819534725">
<node TEXT="dn.commitBlockSynchronization" ID="ID_178215588" CREATED="1496819539064" MODIFIED="1496819546160">
<font BOLD="true"/>
</node>
</node>
</node>
</node>
<node TEXT="completeBlock" ID="ID_1021735568" CREATED="1496818967502" MODIFIED="1496818969889">
<node TEXT="addStoredBlock" ID="ID_1266167639" CREATED="1496818976781" MODIFIED="1496818979033">
<node TEXT="addStoredBlockUnderConstruction" ID="ID_1936892975" CREATED="1496818984949" MODIFIED="1496818990185">
<node TEXT="processAndHandleReportedBlock" ID="ID_1944467507" CREATED="1496818996925" MODIFIED="1496819830094">
<font BOLD="true"/>
</node>
<node TEXT="addStoredBlockUnderConstructionTx" ID="ID_973121362" CREATED="1496819301170" MODIFIED="1496819309495">
<node TEXT="processFullReport" ID="ID_1047000167" CREATED="1496819348466" MODIFIED="1496819356578">
<font BOLD="true"/>
</node>
</node>
</node>
<node TEXT="processAndHandleReportedBlock" ID="ID_1341889084" CREATED="1496818999949" MODIFIED="1496819819254">
<font BOLD="false"/>
<node TEXT="processIncrementalBlockReport" ID="ID_1600618858" CREATED="1496819196203" MODIFIED="1496819386113">
<font BOLD="true"/>
</node>
<node TEXT="addBlock" ID="ID_1611167793" CREATED="1496819230107" MODIFIED="1496819232103">
<node TEXT="processIncrementalBlockReport" ID="ID_1173470945" CREATED="1496819232875" MODIFIED="1496819385369">
<font BOLD="false"/>
<node TEXT="dn.blockReceivedAndDeleted" ID="ID_331275000" CREATED="1496819387666" MODIFIED="1496819394169">
<font BOLD="true"/>
</node>
</node>
</node>
</node>
<node TEXT="addStoredBlockTx" ID="ID_1513364004" CREATED="1496819032630" MODIFIED="1496819035609">
<node TEXT="processFullReport" ID="ID_1477454914" CREATED="1496819040500" MODIFIED="1496819357513">
<font BOLD="true"/>
<node TEXT="processReport" ID="ID_966190374" CREATED="1496819262691" MODIFIED="1496819265982">
<node TEXT="dn.blockReport" ID="ID_1367982429" CREATED="1496819276674" MODIFIED="1496819280066">
<font BOLD="true"/>
</node>
</node>
</node>
</node>
</node>
</node>
</node>
<node TEXT="tryToCompleteBlock" ID="ID_226759237" CREATED="1496818774175" MODIFIED="1496818777523">
<node TEXT="checkFileProgress x2" ID="ID_1957961484" CREATED="1496818785126" MODIFIED="1496818805643">
<node TEXT="analyzeFileState" ID="ID_989357735" CREATED="1496818814182" MODIFIED="1496818817043">
<node TEXT="getAdditionalBlock (x2)" ID="ID_1216532051" CREATED="1496818880678" MODIFIED="1496818906754">
<node TEXT="client.addBlock" ID="ID_1909581205" CREATED="1496818912701" MODIFIED="1496818919013">
<font BOLD="true"/>
</node>
</node>
</node>
<node TEXT="completeFileInternal" ID="ID_1440049702" CREATED="1496818829830" MODIFIED="1496818834603">
<node TEXT="completeFile" ID="ID_672339439" CREATED="1496818840710" MODIFIED="1496818843026">
<node TEXT="client.complete" ID="ID_153278422" CREATED="1496818851830" MODIFIED="1496818857310">
<font BOLD="true"/>
</node>
</node>
</node>
</node>
</node>
</node>
</node>
</node>
</node>
</map>
