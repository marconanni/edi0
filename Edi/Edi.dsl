HighLevelMachine edi;

Request ComandoUserCmd;

Invitation ComandoScontrol;

Invitation Status;

Signal DatiSensore;

UserCmd demand ComandoUserCmd to Scontrol;

Scontrol grant ComandoUserCmd;

Scontrol ask ComandoScontrol to Elettrodomestico;

Elettrodomestico accept ComandoScontrol;

Sensore emit DatiSensore;

Scontrol sense DatiSensore;