> - "Authentication is about identity, Authorization is about permissions"
# Requirements
- Ein User kann sich mit Email und Passwort authentifizieren (Basic Auth)
- Nach erfolgreicher Authentifizierung werden weitere Authentifizierungen mit JWT automatisiert (Additional Auth)
- Eine Authentifizierung hat eine zeitlich begrenzte Gültigkeit
- Es werden mindestens zwei Rollen (Admin, User) unterschieden
	- Admin Bereiche nur für Admin ersichtlich
- Es sind abhängig von den Rollen unterschiedliche Zugriffe möglich
## Zusatzaufgabe
- Multi Factor Authentication
# Ablauf
1. Basic Auth umsetzen (Autorisation) mit in Memory User in Security Config
2. Basic Auth mit DB User umsetzen
3. Basic Auth zu Additional Auth mit JWT upgraden