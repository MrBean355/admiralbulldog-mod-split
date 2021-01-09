apply(plugin = "mod")

configure<ModExtension> {
    fileRenames {
        add("panorama/images/pings/ping_world_png.vtex_c", "panorama/images/pings/ping_world_psd.vtex_c")
    }
}