#!/bin/bash

files=( $(find ./result/ -type f -name "*.nkp") )
total=${#files[@]}
count=0

for file in "${files[@]}"; do
    count=$((count + 1))

    base_name=$(basename "$file" .nkp)

    # Carpeta de salida
    output_dir="./result/cg/${base_name}"

    mkdir -p "$output_dir"

    echo "[$count/$total] Extrayendo $file a $output_dir"

    # Ejecuta quickbms solo para los archivos .cg
    ./tools/quickbms/quickbms.exe -f "{}.cg" ./nkp.bms "$file" "$output_dir" 2>/dev/null || true
done
