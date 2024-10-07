package org.example.clientes.cache;

import org.example.clientes.model.Usuario;

public class CacheUsuarioImpl extends CacheImpl<Long, Usuario> implements CacheUsuario{
    public CacheUsuarioImpl(int cacheSize) {
        super(cacheSize);
    }
}
