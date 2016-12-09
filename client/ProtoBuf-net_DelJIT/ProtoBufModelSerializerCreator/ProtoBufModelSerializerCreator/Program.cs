using Message;
using ProtoBuf.Meta;
using System;
using System.Collections.Generic;
using System.Text;

namespace ProtoBufModelSerializerCreator
{
    class Program
    {
        static void Main(string[] args)
        {
            var model = TypeModel.Create();

            model.Add(typeof(object), true);

            model.Add(typeof(ErrorMessage), true);
            model.Add(typeof (HeartBeatReq), true);
            model.Add(typeof (HeartBeatRes),true);
            model.Add(typeof(LoginReq), true);
            model.Add(typeof(LoginRes), true);
         
            model.AllowParseableTypes = true;
            model.AutoAddMissingTypes = true;
            model.AutoCompile = false;
            model.Compile("ProtoModelSerializer", "ProtoModelSerializer.dll");  
        }
    }
}
