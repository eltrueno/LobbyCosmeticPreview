# Lobby Cosmetic Preview


CPCamera camera = new CPCamera(p.getLocation());
camera.create();

SchematicPreview sc = new SchematicPreview(p, camera, plugin, p.getLocation().clone().add(0, 2, 6), plugin.getDataFolder()+"/cages/test.schematic");
sc.start();
